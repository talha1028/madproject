package com.example.madproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.madproject.firebase.BidManager;
import com.example.madproject.firebase.JobManager;
import com.example.madproject.firebase.UserManager;
import com.example.madproject.models.Bid;
import com.example.madproject.models.Job;
import com.example.madproject.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class SubmitBidActivity extends AppCompatActivity {

    private EditText etBidAmount, etCompletionDays, etProposal;
    private CheckBox cbTerms;
    private Button btnSubmitBid, btnCancel;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private String jobId;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bid);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "";

        // Get job ID from Intent
        jobId = getIntent().getStringExtra("jobId");

        if (jobId == null || jobId.isEmpty()) {
            Toast.makeText(this, "Error: Job ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClickListeners();
        loadJobDetails();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etBidAmount = findViewById(R.id.etBidAmount);
        etCompletionDays = findViewById(R.id.etCompletionDays);
        etProposal = findViewById(R.id.etProposal);
        cbTerms = findViewById(R.id.cbTerms);
        btnSubmitBid = findViewById(R.id.btnSubmitBid);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupClickListeners() {
        btnSubmitBid.setOnClickListener(v -> submitBid());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadJobDetails() {
        JobManager.getInstance()
                .getJob(jobId)
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        job = documentSnapshot.toObject(Job.class);
                        // Display job info in header if UI elements exist
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading job details", Toast.LENGTH_SHORT).show();
                });
    }

    private void submitBid() {
        String amount = etBidAmount.getText().toString().trim();
        String days = etCompletionDays.getText().toString().trim();
        String proposal = etProposal.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(amount)) {
            etBidAmount.setError("Amount required");
            etBidAmount.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept terms", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if already bid on this job
        showLoading(true);

        BidManager.getInstance()
                .checkExistingBid(jobId, currentUserId)
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        showLoading(false);
                        Toast.makeText(this, "You already submitted a bid for this job",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Create bid
                    createBid(amount, days, proposal);
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Error checking existing bid: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void createBid(String amount, String days, String proposal) {
        // Get contractor info
        UserManager.getInstance()
                .getUserObject(currentUserId, new UserManager.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(User contractor) {
                        if (contractor == null) {
                            showLoading(false);
                            Toast.makeText(SubmitBidActivity.this,
                                    "Error loading profile", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String bidId = "bid_" + UUID.randomUUID().toString();

                        Bid bid = new Bid(
                                bidId,
                                jobId,
                                job != null ? job.getTitle() : "",
                                currentUserId,
                                contractor.getFullName(),
                                Double.parseDouble(amount),
                                days.isEmpty() ? 30 : Integer.parseInt(days),
                                proposal
                        );

                        // Set contractor details
                        bid.setContractorCategory(contractor.getCategory());
                        bid.setContractorRating(contractor.getRating());
                        bid.setContractorCompletedProjects(contractor.getCompletedProjects());

                        // Submit bid
                        BidManager.getInstance()
                                .createBid(bid)
                                .addOnSuccessListener(aVoid -> {
                                    // Increment job's totalBids
                                    JobManager.getInstance().incrementTotalBids(jobId);

                                    showLoading(false);
                                    Toast.makeText(SubmitBidActivity.this,
                                            "Bid submitted successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    showLoading(false);
                                    Toast.makeText(SubmitBidActivity.this,
                                            "Error submitting bid: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });
                    }

                    @Override
                    public void onError(String error) {
                        showLoading(false);
                        Toast.makeText(SubmitBidActivity.this,
                                "Error loading profile: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean show) {
        if (show) {
            btnSubmitBid.setEnabled(false);
            btnSubmitBid.setText("Submitting...");
            btnCancel.setEnabled(false);
            etBidAmount.setEnabled(false);
            etCompletionDays.setEnabled(false);
            etProposal.setEnabled(false);
            cbTerms.setEnabled(false);
        } else {
            btnSubmitBid.setEnabled(true);
            btnSubmitBid.setText("Submit Bid");
            btnCancel.setEnabled(true);
            etBidAmount.setEnabled(true);
            etCompletionDays.setEnabled(true);
            etProposal.setEnabled(true);
            cbTerms.setEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}