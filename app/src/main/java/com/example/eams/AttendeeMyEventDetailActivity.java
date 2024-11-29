package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AttendeeMyEventDetailActivity extends AppCompatActivity {

    private String eventInfo;
    private String attendeeEmail;
    private String title="";
    private String status="";
    private DatabaseReference eventReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_my_event_detail);

        eventInfo = getIntent().getStringExtra("EventInfo");
        attendeeEmail = getIntent().getStringExtra("attendeeEmail");

        eventReference = FirebaseDatabase.getInstance().getReference("events");

        TextView userInfo = findViewById(R.id.eventTextView);
        userInfo.setText(eventInfo);

        Pattern pattern = Pattern.compile("Title: (.*?)\\n");
        Matcher matcher = pattern.matcher(eventInfo);

        Pattern pattern2 = Pattern.compile("Status:\\s*(\\w+)");
        Matcher matcher2 = pattern2.matcher(eventInfo);

        if (matcher.find()) {
            title = matcher.group(1).trim();
        }
        if (matcher2.find()) {
            status = matcher2.group(1).trim();
        }
    }

    public void cancel(View view) {


    }
}