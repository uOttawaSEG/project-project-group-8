package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        String role = getIntent().getStringExtra("role");
        email = getIntent().getStringExtra("email");

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome! You are logged in as " + role);
    }

    public void logout(View view) {

        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void eventsInbox(View view) {

        Intent intent = new Intent(WelcomeActivity.this, AttendeeEventsInboxActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void myEventsInbox(View view) {

        Intent intent = new Intent(WelcomeActivity.this, AttendeeMyEventsActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
