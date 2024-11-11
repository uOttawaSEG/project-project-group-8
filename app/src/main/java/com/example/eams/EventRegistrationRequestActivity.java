package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventRegistrationRequestActivity extends AppCompatActivity {

    private String eventInfo;
    private DatabaseReference eventReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration_request);

        Intent receive = getIntent();

        eventReference = FirebaseDatabase.getInstance().getReference("events");

        eventInfo = receive.getStringExtra("EventInfo");
    }

    public void aprroveAll(View view) {


    }
}