package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button  btnLogin;
    private TextView btnCreate;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        btnCreate = findViewById(R.id.createbtn);
        btnLogin = findViewById(R.id.loginbtn);

        auth = FirebaseAuth.getInstance();

        btnCreate.setOnClickListener(v -> createAccount());
        btnLogin.setOnClickListener(v -> loginUser());
    }
    private void createAccount() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);

    }
    private void loginUser() {
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (!validateInputs(e, p)) return;

        auth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Move to dashboard
                        Intent intent = new Intent(MainActivity.this, ClientDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        finish(); // prevent back to login

                    } else {
                        Toast.makeText(this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean validateInputs(String e, String p) {
        if (e.isEmpty()) {
            email.setError("Email required");
            return false;
        }
        if (p.isEmpty()) {
            password.setError("Password required");
            return false;
        }
        if (p.length() < 6) {
            password.setError("Minimum 6 characters");
            return false;
        }
        return true;
    }
}
