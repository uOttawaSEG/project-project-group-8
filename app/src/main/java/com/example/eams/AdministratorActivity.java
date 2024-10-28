package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdministratorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        String role = getIntent().getStringExtra("role");

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome! You are logged in as " + role);
    }

    public void inbox(View view) {

        Intent intent = new Intent(AdministratorActivity.this, InboxActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {

        Intent intent = new Intent(AdministratorActivity.this, MainActivity.class);
        startActivity(intent);
    }
}