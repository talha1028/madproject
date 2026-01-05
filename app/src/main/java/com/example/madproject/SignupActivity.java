package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private Spinner roleSpinner;
    private EditText fullName, email, mobileNumber, password;
    private Button createAccountBtn;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize views
        roleSpinner = findViewById(R.id.roleSpinner);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        mobileNumber = findViewById(R.id.mobileNumber);
        password = findViewById(R.id.password);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        loginLink = findViewById(R.id.loginLink);

        // Setup Role Spinner
        setupRoleSpinner();

        // Create Account Button Click
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullName.getText().toString();
                String emailText = email.getText().toString();
                String mobile = mobileNumber.getText().toString();
                String selectedRole = roleSpinner.getSelectedItem().toString();
                String pass = password.getText().toString();

                // Your signup logic here
            }
        });

        // Login Link Click
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login activity
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Close Button
        ImageView btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRoleSpinner() {
        // Create role options
        String[] roles = {"Select role", "Manager", "Engineer", "Developer", "Designer", "Admin"};

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to spinner
        roleSpinner.setAdapter(adapter);
    }
}
