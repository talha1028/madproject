package com.example.madproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SubmitBidActivity extends AppCompatActivity {

    private EditText etBidAmount, etCompletionDays, etProposal;
    private CheckBox cbTerms;
    private Button btnSubmitBid, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bid);

        initViews();
        setupClickListeners();
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

    private void submitBid() {
        String amount = etBidAmount.getText().toString().trim();
        String days = etCompletionDays.getText().toString().trim();
        String proposal = etProposal.getText().toString().trim();

        if (TextUtils.isEmpty(amount)) {
            etBidAmount.setError("Amount required");
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept terms", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Submit bid to Firebase
        Toast.makeText(this, "Bid submitted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}