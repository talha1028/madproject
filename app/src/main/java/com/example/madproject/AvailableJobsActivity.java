package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.adapters.JobAdapter;
import com.example.madproject.firebase.JobManager;
import com.example.madproject.models.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvailableJobsActivity extends AppCompatActivity {

    private static final String TAG = "AvailableJobs";

    private Toolbar toolbar;
    private EditText etSearch;
    private Spinner spinnerCategory;
    private RecyclerView rvJobs;
    private LinearLayout emptyState;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String currentUserId;

    private JobAdapter jobAdapter;
    private List<Job> allJobsList;
    private List<Job> filteredJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_jobs);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        initViews();
        setupToolbar();
        setupCategorySpinner();
        setupRecyclerView();
        setupSearchFilter();
        loadOpenJobs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh jobs when returning to this activity (e.g., after submitting a bid)
        loadOpenJobs();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etSearch = findViewById(R.id.etSearch);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        rvJobs = findViewById(R.id.rvJob);
        emptyState = findViewById(R.id.emptyState);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Available Jobs");
        }
    }

    private void setupCategorySpinner() {
        String[] categories = {
                "All Categories",
                "Construction",
                "Plumbing",
                "Electrical",
                "Painting",
                "Carpentry",
                "Masonry",
                "Roofing",
                "Flooring",
                "Interior Design",
                "Landscaping",
                "HVAC",
                "Welding",
                "Tiling",
                "Renovation",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                filterJobs();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void setupRecyclerView() {
        allJobsList = new ArrayList<>();
        filteredJobsList = new ArrayList<>();

        jobAdapter = new JobAdapter(this, filteredJobsList, job -> {
            // Navigate to job details
            Intent intent = new Intent(AvailableJobsActivity.this, JobDetailActivity.class);
            intent.putExtra("jobId", job.getJobId());
            startActivity(intent);
        });

        rvJobs.setLayoutManager(new LinearLayoutManager(this));
        rvJobs.setAdapter(jobAdapter);
    }

    private void setupSearchFilter() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterJobs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadOpenJobs() {
        Log.d(TAG, "Loading all open jobs");
        showLoading(true);

        JobManager.getInstance()
                .getOpenJobs()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Open jobs loaded: " + queryDocumentSnapshots.size());
                    showLoading(false);

                    allJobsList.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Job job = doc.toObject(Job.class);
                        if (job != null) {
                            allJobsList.add(job);
                        }
                    }

                    // Sort by posted date (newest first)
                    Collections.sort(allJobsList, (j1, j2) ->
                            Long.compare(j2.getPostedDate(), j1.getPostedDate()));

                    // Apply filters
                    filterJobs();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Log.e(TAG, "Error loading jobs: " + e.getMessage());
                    Toast.makeText(this, "Error loading jobs: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void filterJobs() {
        String searchQuery = etSearch.getText().toString().toLowerCase().trim();
        String selectedCategory = spinnerCategory.getSelectedItem().toString();

        filteredJobsList.clear();

        for (Job job : allJobsList) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    job.getTitle().toLowerCase().contains(searchQuery) ||
                    job.getDescription().toLowerCase().contains(searchQuery) ||
                    job.getLocation().toLowerCase().contains(searchQuery);

            boolean matchesCategory = selectedCategory.equals("All Categories") ||
                    job.getCategory().equals(selectedCategory);

            if (matchesSearch && matchesCategory) {
                filteredJobsList.add(job);
            }
        }

        jobAdapter.notifyDataSetChanged();

        // Show/hide empty state
        if (filteredJobsList.isEmpty()) {
            rvJobs.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            rvJobs.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }

        Log.d(TAG, "Filtered jobs: " + filteredJobsList.size());
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            rvJobs.setVisibility(View.GONE);
            emptyState.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}