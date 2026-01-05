package com.example.madproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private TextView btnMarkAllRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        initViews();
        loadNotifications();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvNotifications = findViewById(R.id.rvTodayNotifications);
        btnMarkAllRead = findViewById(R.id.btnMarkAllRead);

        rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        btnMarkAllRead.setOnClickListener(v -> {
            // TODO: Mark all notifications as read
        });
    }

    private void loadNotifications() {
        // TODO: Load notifications from Firebase
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
