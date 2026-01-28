
package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.adapters.TaskAdapter;
import com.example.madproject.firebase.TaskManager;
import com.example.madproject.models.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private FloatingActionButton fabAddTask;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private LinearLayout emptyState;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private String jobId;
    private String projectName;

    private TaskAdapter taskAdapter;
    private List<Task> allTasksList;
    private List<Task> filteredTasksList;
    private String currentFilter = "all"; // all, ongoing, completed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        // Get jobId and projectName from Intent
        jobId = getIntent().getStringExtra("jobId");
        projectName = getIntent().getStringExtra("projectName");

        if (jobId == null || jobId.isEmpty()) {
            Toast.makeText(this, "Error: Job ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupTabs();
        setupRecyclerView();
        setupClickListeners();
        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh tasks when returning to this activity
        loadTasks();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvTasks = findViewById(R.id.rvTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
        tabLayout = findViewById(R.id.tabLayout);

        // Try to find ProgressBar and empty state, create if not in layout
        progressBar = findViewById(R.id.progressBar);
        if (progressBar == null) {
            progressBar = new ProgressBar(this);
            progressBar.setVisibility(View.GONE);
        }

        emptyState = findViewById(R.id.emptyState);
        if (emptyState == null) {
            emptyState = new LinearLayout(this);
            emptyState.setVisibility(View.GONE);
        }

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentFilter = "all";
                        break;
                    case 1:
                        currentFilter = "ongoing";
                        break;
                    case 2:
                        currentFilter = "completed";
                        break;
                }
                filterTasks();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupRecyclerView() {
        allTasksList = new ArrayList<>();
        filteredTasksList = new ArrayList<>();

        taskAdapter = new TaskAdapter(this, filteredTasksList, task -> {
            // Navigate to task detail
            Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
            intent.putExtra("taskId", task.getTaskId());
            startActivity(intent);
        });

        rvTasks.setAdapter(taskAdapter);
    }

    private void setupClickListeners() {
        fabAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskActivity.class);
            intent.putExtra("jobId", jobId);
            intent.putExtra("projectName", projectName);
            startActivity(intent);
        });
    }

    private void loadTasks() {
        showLoading(true);

        TaskManager.getInstance()
                .getTasksByJob(jobId)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    showLoading(false);
                    allTasksList.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Task task = doc.toObject(Task.class);
                        if (task != null) {
                            allTasksList.add(task);
                        }
                    }

                    // Sort by updated date (newest first)
                    Collections.sort(allTasksList, (t1, t2) ->
                            Long.compare(t2.getUpdatedAt(), t1.getUpdatedAt()));

                    // Apply filter
                    filterTasks();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error loading tasks: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void filterTasks() {
        filteredTasksList.clear();

        for (Task task : allTasksList) {
            boolean matchesFilter = currentFilter.equals("all") ||
                                  task.getStatus().equals(currentFilter);

            if (matchesFilter) {
                filteredTasksList.add(task);
            }
        }

        taskAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            rvTasks.setVisibility(View.GONE);
            emptyState.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void updateEmptyState() {
        if (filteredTasksList.isEmpty()) {
            rvTasks.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvTasks.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
