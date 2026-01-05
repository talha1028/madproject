package com.example.madproject;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContractorDirectoryActivity extends AppCompatActivity {

    private RecyclerView rvContractors;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_directory);

        initViews();
        loadContractors();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvContractors = findViewById(R.id.rvContractors);
        etSearch = findViewById(R.id.etSearch);

        rvContractors.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadContractors() {
        // TODO: Load contractors from Firebase
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}