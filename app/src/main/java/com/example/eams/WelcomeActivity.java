package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        String role = getIntent().getStringExtra("role");

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome, " + role + "!");
    }

    public void logout(View view) {

        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
