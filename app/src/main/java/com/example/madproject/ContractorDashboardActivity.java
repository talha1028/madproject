package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContractorDashboardActivity extends AppCompatActivity {

    private TextView tvContractorName, tvRating, tvActiveProjects, tvCompletedProjects, tvTotalEarnings;
    private Button btnViewProfile;
    private RecyclerView rvAvailableJobs;
    private BottomNavigationView bottomNav;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        initViews();

        // Setup listeners
        setupClickListeners();

        // Load data
        loadDashboardData();
    }

    private void initViews() {
        tvContractorName = findViewById(R.id.tvContractorName);
        tvRating = findViewById(R.id.tvRating);
        tvActiveProjects = findViewById(R.id.tvActiveProjects);
        tvCompletedProjects = findViewById(R.id.tvCompletedProjects);
        tvTotalEarnings = findViewById(R.id.tvTotalEarnings);
        btnViewProfile = findViewById(R.id.btnViewProfile);
        rvAvailableJobs = findViewById(R.id.rvAvailableJobs);
        bottomNav = findViewById(R.id.bottomNav);

        // Setup RecyclerView
        rvAvailableJobs.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        // View Profile Button
        btnViewProfile.setOnClickListener(v -> {
            startActivity(new Intent(ContractorDashboardActivity.this, ContractorProfileActivity.class));
        });

        // Bottom Navigation
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_jobs) {
                // Already on jobs screen
                return true;
            } else if (id == R.id.nav_projects) {
                startActivity(new Intent(ContractorDashboardActivity.this, TaskListActivity.class));
                return true;
            } else if (id == R.id.nav_messages) {
                startActivity(new Intent(ContractorDashboardActivity.this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ContractorDashboardActivity.this, SettingsActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadDashboardData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvContractorName.setText(user.getDisplayName());

            // TODO: Load from Firestore
            tvRating.setText("4.8");
            tvActiveProjects.setText("3");
            tvCompletedProjects.setText("15");
            tvTotalEarnings.setText("Rs. 250K");
        }
    }
}