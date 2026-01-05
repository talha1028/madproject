package com.example.madproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TaskDetailActivity extends AppCompatActivity {

    private TextView tvTaskTitle, tvTaskDescription, tvStartDate, tvEndDate, tvWorkers, tvDailyWages;
    private Button btnUpdateProgress, btnMarkComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        initViews();
        loadTaskDetails();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvTaskDescription = findViewById(R.id.tvTaskDescription);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvWorkers = findViewById(R.id.tvWorkers);
        tvDailyWages = findViewById(R.id.tvDailyWages);
        btnUpdateProgress = findViewById(R.id.btnUpdateProgress);
        btnMarkComplete = findViewById(R.id.btnMarkComplete);
    }

    private void loadTaskDetails() {
        // TODO: Load from Firebase
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
