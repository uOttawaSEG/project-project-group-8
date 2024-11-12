package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventRegistrationRequestAttendeeDetailActivity extends AppCompatActivity {

    private String attendeeEmail;
    private String title;
    private DatabaseReference attendeeReference;
    private DatabaseReference eventReference;
    private TextView attendeeDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration_request_attendee_detail);

        attendeeEmail = getIntent().getStringExtra("attendeeEmail");
        title = getIntent().getStringExtra("EventTitle");

        attendeeReference = FirebaseDatabase.getInstance().getReference("attendees");

        eventReference = FirebaseDatabase.getInstance().getReference("events");

        attendeeDetailView = findViewById(R.id.AttendeeDetailView);

        attendeeDetail();
    }

    private void attendeeDetail() {

        Query query = attendeeReference.orderByChild("email").equalTo(attendeeEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {

                        String id = attendeeSnapshot.getKey();
                        Attendee attendee = attendeeSnapshot.getValue(Attendee.class);

                        String attendeeInfo = "Name: " + attendee.getFirstName() + " " + attendee.getLastName()
                                            + "\nEmail: " + attendee.getEmail()
                                            + "\nPhone: " + attendee.getPhoneNumber()
                                            + "\nAddress: " + attendee.getAddress();

                        attendeeDetailView.setText(attendeeInfo);
                    }
                } else {
                    Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "No attendee found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void approve(View view) {

        Query query = eventReference.orderByChild("title").equalTo(title);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                        String id = eventSnapshot.getKey();

                        EventInfo event = eventSnapshot.getValue(EventInfo.class);

                        List<String> registrationRequests = new ArrayList<>();

                        if(event.getRegistrationRequests() != null) {

                            registrationRequests = event.getRegistrationRequests();

                            registrationRequests.remove(attendeeEmail);

                            event.setRegistrationRequests(registrationRequests);
                        }

                        List<String> attendeesList = new ArrayList<>();

                        if(event.getAttendees() != null) {

                            attendeesList = event.getAttendees();
                        }

                        attendeesList.add(attendeeEmail);

                        event.setAttendees(attendeesList);

                        eventReference.child(id).removeValue();

                        event.saveEventToDatabase();

                        Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "Approved Successfully", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                } else {
                    Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void reject(View view) {

        Query query = eventReference.orderByChild("title").equalTo(title);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                        String id = eventSnapshot.getKey();

                        EventInfo event = eventSnapshot.getValue(EventInfo.class);

                        List<String> registrationRequests = new ArrayList<>();

                        if(event.getRegistrationRequests() != null) {

                            registrationRequests = event.getRegistrationRequests();

                            registrationRequests.remove(attendeeEmail);

                            event.setRegistrationRequests(registrationRequests);

                            eventReference.child(id).removeValue();

                            event.saveEventToDatabase();

                            Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "Rejected Successfully", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                } else {
                    Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventRegistrationRequestAttendeeDetailActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}