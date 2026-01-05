package com.example.madproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddMaterialActivity extends AppCompatActivity {

    private EditText etMaterialName, etQuantity, etUnitPrice, etSupplier, etDescription;
    private Spinner spinnerCategory, spinnerUnit;
    private Button btnSaveMaterial, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etMaterialName = findViewById(R.id.etMaterialName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnitPrice = findViewById(R.id.etUnitPrice);
        etSupplier = findViewById(R.id.etSupplier);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        btnSaveMaterial = findViewById(R.id.btnSaveMaterial);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupClickListeners() {
        btnSaveMaterial.setOnClickListener(v -> saveMaterial());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveMaterial() {
        // TODO: Save material to Firebase
        Toast.makeText(this, "Material saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}