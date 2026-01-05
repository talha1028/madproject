package com.example.madproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTaskTitle, etWorkersCount, etTaskDescription, etEstimatedQuantity, etDailyWages;
    private Spinner spinnerProgressUnit;
    private Button btnCreateTask, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTaskTitle = findViewById(R.id.etTaskTitle);
        etWorkersCount = findViewById(R.id.etWorkersCount);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        etEstimatedQuantity = findViewById(R.id.etEstimatedQuantity);
        etDailyWages = findViewById(R.id.etDailyWages);
        spinnerProgressUnit = findViewById(R.id.spinnerProgressUnit);
        btnCreateTask = findViewById(R.id.btnCreateTask);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupClickListeners() {
        btnCreateTask.setOnClickListener(v -> createTask());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void createTask() {
        // TODO: Create task in Firebase
        Toast.makeText(this, "Task created!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


