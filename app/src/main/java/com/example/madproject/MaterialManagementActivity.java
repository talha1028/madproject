package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;

public class MaterialManagementActivity extends AppCompatActivity {

    private RecyclerView rvMaterials;
    private Button btnRequestMaterial, btnAddMaterial;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_management);

        initViews();
        setupClickListeners();
        loadMaterials();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvMaterials = findViewById(R.id.rvMaterials);
        btnRequestMaterial = findViewById(R.id.btnRequestMaterial);
        btnAddMaterial = findViewById(R.id.btnAddMaterial);
        tabLayout = findViewById(R.id.tabLayout);

        rvMaterials.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        btnAddMaterial.setOnClickListener(v -> {
            startActivity(new Intent(this, AddMaterialActivity.class));
        });
    }

    private void loadMaterials() {
        // TODO: Load materials from Firebase
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
