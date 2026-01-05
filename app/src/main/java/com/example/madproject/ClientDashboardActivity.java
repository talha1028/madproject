package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ClientDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvUserName, tvViewAllJobs;
    private ImageView btnNotifications;
    private RecyclerView rvMyJobs;
    private CardView btnPostJob, cardFindContractors;
    private BottomNavigationView bottomNavigation;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        initViews();

        // Setup click listeners
        setupClickListeners();

        // Load user data
        loadDashboardData();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserName = findViewById(R.id.tvUserName);
        tvViewAllJobs = findViewById(R.id.tvViewAllJobs);
        btnNotifications = findViewById(R.id.btnNotifications);
        rvMyJobs = findViewById(R.id.rvMyJobs);
        btnPostJob = findViewById(R.id.btnPostJob);
        cardFindContractors = findViewById(R.id.cardFindContractors);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Setup RecyclerView
        rvMyJobs.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        // Notifications Button
        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(ClientDashboardActivity.this, NotificationsActivity.class));
        });

        // Post Job Button (CardView)
        btnPostJob.setOnClickListener(v -> {
            startActivity(new Intent(ClientDashboardActivity.this, JobPostActivity.class));
        });

        // Find Contractors Card
        cardFindContractors.setOnClickListener(v -> {
            startActivity(new Intent(ClientDashboardActivity.this, ContractorDirectoryActivity.class));
        });

        // View All Jobs
        tvViewAllJobs.setOnClickListener(v -> {
            // TODO: Navigate to All Jobs screen
        });

        // Bottom Navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_my_jobs) {
                // TODO: Navigate to My Jobs
                return true;
            } else if (id == R.id.nav_find) {
                startActivity(new Intent(ClientDashboardActivity.this, ContractorDirectoryActivity.class));
                return true;
            } else if (id == R.id.nav_messages) {
                startActivity(new Intent(ClientDashboardActivity.this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(ClientDashboardActivity.this, SettingsActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadDashboardData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                tvUserName.setText(displayName);
            } else {
                tvUserName.setText("User");
            }

            // TODO: Load actual jobs from Firestore
            // For now, RecyclerView will be empty until you add an adapter
        }
    }
}