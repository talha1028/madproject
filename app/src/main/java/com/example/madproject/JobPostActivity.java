package com.example.madproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class JobPostActivity extends AppCompatActivity {

    private EditText etJobTitle, etJobDescription, etBudget, etTimeline, etLocation;
    private Spinner spinnerCategory;
    private Button btnPostJob, btnCancel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etJobTitle = findViewById(R.id.etJobTitle);
        etJobDescription = findViewById(R.id.etJobDescription);
        etBudget = findViewById(R.id.etBudget);
        etTimeline = findViewById(R.id.etTimeline);
        etLocation = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnPostJob = findViewById(R.id.btnPostJob);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupClickListeners() {
        btnPostJob.setOnClickListener(v -> postJob());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void postJob() {
        String title = etJobTitle.getText().toString().trim();
        String description = etJobDescription.getText().toString().trim();
        String budget = etBudget.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etJobTitle.setError("Title required");
            return;
        }

        // TODO: Post job to Firebase
        Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}