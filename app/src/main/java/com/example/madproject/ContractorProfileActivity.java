package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContractorProfileActivity extends AppCompatActivity {

    private TextView tvName, tvCategory, tvRating, tvExperience, tvHourlyRate, tvBio;
    private RecyclerView rvPortfolio, rvReviews;
    private Button btnCall, btnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_profile);

        initViews();
        setupClickListeners();
        loadProfile();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tvName);
        tvCategory = findViewById(R.id.tvCategory);
        tvRating = findViewById(R.id.tvRating);
        tvExperience = findViewById(R.id.tvExperience);
        tvHourlyRate = findViewById(R.id.tvHourlyRate);
        tvBio = findViewById(R.id.tvBio);
        btnCall= findViewById(R.id.btnCall);
        btnMessage= findViewById(R.id.btnMessage);
        rvPortfolio = findViewById(R.id.rvPortfolio);
        rvReviews = findViewById(R.id.rvReviews);

        rvPortfolio.setLayoutManager(new GridLayoutManager(this, 3));
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnMessage.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatActivity.class));
        });

    }

    private void loadProfile() {
        // TODO: Load profile from Firebase
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}