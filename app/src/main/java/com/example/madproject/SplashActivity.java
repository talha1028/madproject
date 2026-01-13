package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madproject.firebase.UserManager;
import com.example.madproject.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Delay and check authentication
        new Handler(Looper.getMainLooper()).postDelayed(() -> checkUserAuthentication(), SPLASH_DELAY);
    }

    private void checkUserAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            // User is signed in, check user type and navigate to appropriate dashboard
            Log.d(TAG, "User authenticated: " + currentUser.getUid());
            loadUserAndNavigate(currentUser.getUid());
        } else {
            // No user signed in, go to login
            Log.d(TAG, "No user authenticated, navigating to login");
            navigateToLogin();
        }
    }

    private void loadUserAndNavigate(String userId) {
        Log.d(TAG, "Loading user data for: " + userId);

        UserManager.getInstance()
                .getUserObject(userId, new UserManager.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(User user) {
                        Log.d(TAG, "User loaded: " + user.getFullName() + " - Type: " + user.getUserType());

                        Intent intent;

                        // Navigate based on user type
                        if ("contractor".equalsIgnoreCase(user.getUserType())) {
                            Log.d(TAG, "Navigating to ContractorDashboardActivity");
                            intent = new Intent(SplashActivity.this, ContractorDashboardActivity.class);
                        } else if ("client".equalsIgnoreCase(user.getUserType())) {
                            Log.d(TAG, "Navigating to ClientDashboardActivity");
                            intent = new Intent(SplashActivity.this, ClientDashboardActivity.class);
                        } else {
                            // Unknown user type, go to login
                            Log.e(TAG, "Unknown user type: " + user.getUserType());
                            Toast.makeText(SplashActivity.this,
                                    "Unknown user type. Please login again.",
                                    Toast.LENGTH_SHORT).show();
                            navigateToLogin();
                            return;
                        }

                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "Error loading user: " + error);

                        // If error loading user data, go to login
                        Toast.makeText(SplashActivity.this,
                                "Error loading user data. Please login again.",
                                Toast.LENGTH_SHORT).show();
                        navigateToLogin();
                    }
                });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}