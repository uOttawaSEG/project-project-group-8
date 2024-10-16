package com.example.eams; // Change to your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eams.R;

public class WelcomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity); // Ensure this matches your XML filename

        // Initialize the welcomeTextView
        welcomeTextView = findViewById(R.id.welcomeTextView);

        // Get the role from the Intent
        String role = getIntent().getStringExtra("role");

        // Check if the role is not null
        if (role != null) {
            welcomeTextView.setText("Welcome! You are logged in as " + role + ".");
        }
    }
}