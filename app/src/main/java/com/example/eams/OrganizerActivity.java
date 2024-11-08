package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrganizerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);
    }

    public void createEvent(View view) {

        Intent intent = new Intent(OrganizerActivity.this, CreateEventActivity.class);
        startActivity(intent);
    }

    public void upcomingEvents(View view) {

        Intent intent = new Intent(OrganizerActivity.this, UpcomingEventsActivity.class);
        startActivity(intent);
    }

    public void pastEvents(View view) {

        Intent intent = new Intent(OrganizerActivity.this, PastEventsActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {

        Intent intent = new Intent(OrganizerActivity.this, MainActivity.class);
        startActivity(intent);
    }
}