package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JobDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvJobTitle, tvCategory, tvPostedDate, tvDescription, tvBudget, tvTimeline, tvTotalBids, tvLocation;
    private RecyclerView rvBids;
    private ImageView btnEdit, btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        initViews();
        setupClickListeners();
        loadJobDetails();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvCategory = findViewById(R.id.tvCategory);
        tvPostedDate = findViewById(R.id.tvPostedDate);
        tvDescription = findViewById(R.id.tvDescription);
        tvBudget = findViewById(R.id.tvBudget);
        tvTimeline = findViewById(R.id.tvTimeline);
        tvTotalBids = findViewById(R.id.tvTotalBids);
        tvLocation = findViewById(R.id.tvLocation);
        rvBids = findViewById(R.id.rvBids);
        btnEdit = findViewById(R.id.btnEdit);
        btnShare = findViewById(R.id.btnShare);

        rvBids.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnEdit.setOnClickListener(v -> {
            // TODO: Edit job
        });

        btnShare.setOnClickListener(v -> {
            // TODO: Share job
        });
    }

    private void loadJobDetails() {
        // TODO: Load job details from Firebase
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}