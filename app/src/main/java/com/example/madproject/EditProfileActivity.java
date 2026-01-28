package com.example.madproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.madproject.firebase.UserManager;
import com.example.madproject.models.User;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView ivProfilePicture;
    private EditText etFullName, etEmail, etPhone, etAddress;
    private Spinner spinnerCity;
    private Button btnSaveProfile, btnCancel;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        if (currentUserId.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupCitySpinner();
        setupClickListeners();
        loadProfile();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        spinnerCity = findViewById(R.id.spinnerCity);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnCancel = findViewById(R.id.btnCancel);

        // Try to find ProgressBar
        progressBar = findViewById(R.id.progressBar);
        if (progressBar == null) {
            progressBar = new ProgressBar(this);
            progressBar.setVisibility(View.GONE);
        }

        // Email field should be read-only
        etEmail.setEnabled(false);
    }

    private void setupCitySpinner() {
        // Same cities list as JobPostActivity
        String[] cities = {
                "Select City", "Karachi", "Lahore", "Islamabad", "Rawalpindi",
                "Faisalabad", "Multan", "Peshawar", "Quetta", "Sialkot",
                "Gujranwala", "Hyderabad", "Bahawalpur", "Sargodha"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                cities
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSaveProfile.setOnClickListener(v -> saveProfile());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadProfile() {
        showLoading(true);

        UserManager.getInstance()
                .getUserObject(currentUserId, new UserManager.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(User user) {
                        showLoading(false);
                        currentUser = user;
                        populateFields(user);
                    }

                    @Override
                    public void onError(String error) {
                        showLoading(false);
                        Toast.makeText(EditProfileActivity.this,
                                "Error loading profile: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateFields(User user) {
        etFullName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhoneNumber());
        etAddress.setText(user.getAddress());

        // Set spinner value
        setSpinnerValue(spinnerCity, user.getCity());

        // Load profile picture (placeholder for now)
        // TODO: Use Glide/Picasso to load image
        // if (user.getProfilePictureUrl() != null) {
        //     Glide.with(this).load(user.getProfilePictureUrl()).into(ivProfilePicture);
        // }
    }

    private void saveProfile() {
        if (!validateInputs()) {
            return;
        }

        // Update user object
        currentUser.setFullName(etFullName.getText().toString().trim());
        currentUser.setPhoneNumber(etPhone.getText().toString().trim());
        currentUser.setAddress(etAddress.getText().toString().trim());
        currentUser.setCity(spinnerCity.getSelectedItem().toString());

        showLoading(true);

        UserManager.getInstance()
                .updateUser(currentUser)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error updating profile: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private boolean validateInputs() {
        // Validate full name
        if (TextUtils.isEmpty(etFullName.getText().toString())) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }

        // Validate phone number
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return false;
        }

        // Validate city selection
        if (spinnerCity.getSelectedItem().toString().equals("Select City")) {
            Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        if (value == null) return;

        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnSaveProfile.setEnabled(false);
            btnSaveProfile.setText("Saving...");
            btnCancel.setEnabled(false);
            etFullName.setEnabled(false);
            etPhone.setEnabled(false);
            etAddress.setEnabled(false);
            spinnerCity.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSaveProfile.setEnabled(true);
            btnSaveProfile.setText("Save Profile");
            btnCancel.setEnabled(true);
            etFullName.setEnabled(true);
            etPhone.setEnabled(true);
            etAddress.setEnabled(true);
            spinnerCity.setEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
