package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PortfolioGalleryActivity extends AppCompatActivity {

    private RecyclerView rvPortfolio;
    private FloatingActionButton fabAddPortfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_gallery);

        initViews();
        loadPortfolio();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvPortfolio = findViewById(R.id.rvPortfolio);
        fabAddPortfolio = findViewById(R.id.fabAddPortfolio);

        rvPortfolio.setLayoutManager(new GridLayoutManager(this, 2));

        fabAddPortfolio.setOnClickListener(v -> {
            // TODO: Add portfolio item
        });
    }

    private void loadPortfolio() {
        // TODO: Load from Firebase
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
