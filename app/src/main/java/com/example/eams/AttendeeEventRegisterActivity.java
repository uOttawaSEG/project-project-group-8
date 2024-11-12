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

public class AttendeeEventRegisterActivity extends AppCompatActivity {

    private String eventInfo;
    private String attendeeEmail;
    private String title="";
    private DatabaseReference eventReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_register);

        eventInfo = getIntent().getStringExtra("EventInfo");
        attendeeEmail = getIntent().getStringExtra("attendeeEmail");

        eventReference = FirebaseDatabase.getInstance().getReference("events");

        TextView userInfo = findViewById(R.id.eventTextView);
        userInfo.setText(eventInfo);

        Pattern pattern = Pattern.compile("Title: (.*?)\\n");
        Matcher matcher = pattern.matcher(eventInfo);

        if (matcher.find()) {
            title = matcher.group(1).trim();
        }
    }

    public void register(View view) {

        Query query = eventReference.orderByChild("title").equalTo(title);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                        String id = eventSnapshot.getKey();
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);

                        if(event.isAutoApprove()) {
                            event.addAttendee(attendeeEmail);
                            event.saveEventToDatabase();
                            Toast.makeText(AttendeeEventRegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            event.addRegistrationRequest(attendeeEmail);
                            event.saveEventToDatabase();
                            Toast.makeText(AttendeeEventRegisterActivity.this, "Registration Request Sent", Toast.LENGTH_SHORT).show();
                        }

                        eventReference.child(id).removeValue();
                    }
                } else {
                    Toast.makeText(AttendeeEventRegisterActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AttendeeEventRegisterActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}