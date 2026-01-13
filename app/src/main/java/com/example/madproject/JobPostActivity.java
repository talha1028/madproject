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

import com.example.madproject.firebase.JobManager;
import com.example.madproject.firebase.UserManager;
import com.example.madproject.models.Job;
import com.example.madproject.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class JobPostActivity extends AppCompatActivity {

    private EditText etJobTitle, etJobDescription, etBudget, etTimeline, etAddress;
    private Spinner spinnerCategory, spinnerCity;
    private Button btnPostJob, btnCancel, btnTakePhoto, btnAddPhoto;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private String currentUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        initViews();
        setupSpinners();
        setupClickListeners();
        loadUserData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etJobTitle = findViewById(R.id.etJobTitle);
        etJobDescription = findViewById(R.id.etJobDescription);
        etBudget = findViewById(R.id.etBudget);
        etTimeline = findViewById(R.id.etTimeline);
        etAddress = findViewById(R.id.etAddress);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCity = findViewById(R.id.spinnerCity);
        btnPostJob = findViewById(R.id.btnPostJob);
        btnCancel = findViewById(R.id.btnCancel);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);

        // Create ProgressBar programmatically if not in XML
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);
    }

    private void setupSpinners() {
        // Setup Category Spinner
        String[] categories = {
                "Select Category",
                "Construction",
                "Plumbing",
                "Electrical",
                "Painting",
                "Carpentry",
                "Masonry",
                "Roofing",
                "Flooring",
                "Interior Design",
                "Landscaping",
                "HVAC",
                "Welding",
                "Tiling",
                "Renovation",
                "Other"
        };

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Setup City Spinner
        String[] cities = {
                "Select City",
                "Karachi",
                "Lahore",
                "Islamabad",
                "Rawalpindi",
                "Faisalabad",
                "Multan",
                "Peshawar",
                "Quetta",
                "Sialkot",
                "Gujranwala",
                "Hyderabad",
                "Bahawalpur",
                "Sargodha",
                "Sukkur",
                "Larkana",
                "Sheikhupura",
                "Rahim Yar Khan",
                "Jhang",
                "Dera Ghazi Khan",
                "Gujrat",
                "Sahiwal",
                "Wah Cantonment",
                "Mardan",
                "Kasur",
                "Okara",
                "Mingora",
                "Nawabshah",
                "Chiniot",
                "Kotri",
                "Khanpur",
                "Hafizabad",
                "Sadiqabad",
                "Mirpur Khas",
                "Burewala",
                "Kohat",
                "Khanewal",
                "Dera Ismail Khan",
                "Turbat",
                "Muzaffargarh",
                "Abbottabad",
                "Mandi Bahauddin",
                "Shikarpur",
                "Jacobabad",
                "Jhelum",
                "Khanpur",
                "Khairpur",
                "Khuzdar",
                "Pakpattan",
                "Attock"
        };

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                cities
        );
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);
    }

    private void setupClickListeners() {
        btnPostJob.setOnClickListener(v -> postJob());
        btnCancel.setOnClickListener(v -> finish());

        // TODO: Implement photo functionality
        btnTakePhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Camera feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        btnAddPhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Gallery feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {
        if (TextUtils.isEmpty(currentUserId)) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        UserManager.getInstance()
                .getUserObject(currentUserId, new UserManager.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(User user) {
                        if (user != null) {
                            currentUserName = user.getFullName();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        // Continue anyway, userName will be empty
                    }
                });
    }

    private void postJob() {
        // Get input values
        String title = etJobTitle.getText().toString().trim();
        String description = etJobDescription.getText().toString().trim();
        String budgetStr = etBudget.getText().toString().trim();
        String timelineStr = etTimeline.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String city = spinnerCity.getSelectedItem().toString();

        // Validate inputs
        if (!validateInputs(title, description, budgetStr, timelineStr, address, category, city)) {
            return;
        }

        // Parse numeric values
        double budget = Double.parseDouble(budgetStr);
        String timeline = timelineStr + " days";

        // Create location string (city + address)
        String location = city + ", " + address;

        // Generate unique job ID
        String jobId = "job_" + UUID.randomUUID().toString();

        // Create Job object
        Job job = new Job(
                jobId,
                currentUserId,
                currentUserName,
                title,
                description,
                category,
                budget,
                timeline,
                location
        );

        // Show loading
        showLoading(true);

        // Save job to Firestore
        JobManager.getInstance()
                .createJob(job)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);

                    // Update client's active jobs count
                    updateClientJobCount();

                    Toast.makeText(JobPostActivity.this,
                            "Job posted successfully!",
                            Toast.LENGTH_SHORT).show();

                    // Return to previous screen
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(JobPostActivity.this,
                            "Error posting job: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void updateClientJobCount() {
        // Increment active jobs count for client
        UserManager.getInstance()
                .getUser(currentUserId)
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            user.setActiveJobs(user.getActiveJobs() + 1);
                            UserManager.getInstance().updateUser(user);
                        }
                    }
                });
    }

    private boolean validateInputs(String title, String description, String budgetStr,
                                   String timelineStr, String address, String category, String city) {
        // Validate job title
        if (TextUtils.isEmpty(title)) {
            etJobTitle.setError("Job title is required");
            etJobTitle.requestFocus();
            return false;
        }

        if (title.length() < 10) {
            etJobTitle.setError("Title must be at least 10 characters");
            etJobTitle.requestFocus();
            return false;
        }

        // Validate category
        if (category.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate description
        if (TextUtils.isEmpty(description)) {
            etJobDescription.setError("Description is required");
            etJobDescription.requestFocus();
            return false;
        }

        if (description.length() < 20) {
            etJobDescription.setError("Description must be at least 20 characters");
            etJobDescription.requestFocus();
            return false;
        }

        // Validate budget
        if (TextUtils.isEmpty(budgetStr)) {
            etBudget.setError("Budget is required");
            etBudget.requestFocus();
            return false;
        }

        try {
            double budget = Double.parseDouble(budgetStr);
            if (budget <= 0) {
                etBudget.setError("Budget must be greater than 0");
                etBudget.requestFocus();
                return false;
            }
            if (budget < 1000) {
                etBudget.setError("Minimum budget is PKR 1,000");
                etBudget.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etBudget.setError("Invalid budget amount");
            etBudget.requestFocus();
            return false;
        }

        // Validate city
        if (city.equals("Select City")) {
            Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate address
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return false;
        }

        // Validate timeline
        if (TextUtils.isEmpty(timelineStr)) {
            etTimeline.setError("Timeline is required");
            etTimeline.requestFocus();
            return false;
        }

        try {
            int timeline = Integer.parseInt(timelineStr);
            if (timeline <= 0) {
                etTimeline.setError("Timeline must be greater than 0");
                etTimeline.requestFocus();
                return false;
            }
            if (timeline > 365) {
                etTimeline.setError("Maximum timeline is 365 days");
                etTimeline.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etTimeline.setError("Invalid timeline");
            etTimeline.requestFocus();
            return false;
        }

        return true;
    }

    private void showLoading(boolean show) {
        if (show) {
            btnPostJob.setEnabled(false);
            btnPostJob.setText("Posting...");
            btnCancel.setEnabled(false);
            etJobTitle.setEnabled(false);
            etJobDescription.setEnabled(false);
            etBudget.setEnabled(false);
            etTimeline.setEnabled(false);
            etAddress.setEnabled(false);
            spinnerCategory.setEnabled(false);
            spinnerCity.setEnabled(false);
            btnTakePhoto.setEnabled(false);
            btnAddPhoto.setEnabled(false);
        } else {
            btnPostJob.setEnabled(true);
            btnPostJob.setText("Post Job");
            btnCancel.setEnabled(true);
            etJobTitle.setEnabled(true);
            etJobDescription.setEnabled(true);
            etBudget.setEnabled(true);
            etTimeline.setEnabled(true);
            etAddress.setEnabled(true);
            spinnerCategory.setEnabled(true);
            spinnerCity.setEnabled(true);
            btnTakePhoto.setEnabled(true);
            btnAddPhoto.setEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}