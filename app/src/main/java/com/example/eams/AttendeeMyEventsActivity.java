package com.example.eams;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendeeMyEventsActivity extends AppCompatActivity {

    private ListView myEventsListView;
    private DatabaseReference eventsRef;
    private List<String> myEventsList;
    private ArrayAdapter<String> adapter;
    private String attendeeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_my_events);

        attendeeEmail = getIntent().getStringExtra("email");

        myEventsListView = findViewById(R.id.MyEventsListView);
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        myEventsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myEventsList);
        myEventsListView.setAdapter(adapter);

        myEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(AttendeeMyEventsActivity.this, AttendeeMyEventDetailActivity.class);
                intent.putExtra("EventInfo", item);
                intent.putExtra("attendeeEmail", attendeeEmail);
                startActivity(intent);
            }
        });

        loadMyEvents();
    }

    private void loadMyEvents() {
        eventsRef.orderByChild("startTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myEventsList.clear();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                if(dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);
                        if (event != null) {
                            String eventDate = dateFormat.format(new Date(event.getDate()));
                            String startTime = timeFormat.format(new Date(event.getStartTime()));
                            String endTime = timeFormat.format(new Date(event.getEndTime()));

                            List<String> attendees = event.getAttendees();
                            List<String> requestList = event.getRegistrationRequests();
                            List<String> rejectedList = event.getRejectedRequests();
                            String eventDetails;
                            String status = "None";

                            if(attendees != null) {

                                if(attendees.contains(attendeeEmail)) {

                                    status = "Approved";
                                }
                            }

                            else if(requestList != null) {

                                if(requestList.contains(attendeeEmail)) {

                                    status = "Pending";
                                }

                            }

                            else if(rejectedList != null) {

                                if(rejectedList.contains(attendeeEmail)) {

                                    status = "Rejected";
                                }

                            }

                            if(!status.equals("None")) {

                                eventDetails = "Title: " + event.getTitle() +
                                        "\nDate: " + eventDate +
                                        "\nStart Time: " + startTime +
                                        "\nEnd Time: " + endTime +
                                        "\nAddress: " + event.getAddress() +
                                        "\nDescription: " + event.getDescription() +
                                        "\nStatus: "+ status;
                                myEventsList.add(eventDetails);
                            }

                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(AttendeeMyEventsActivity.this, "No events to load", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendeeMyEventsActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}