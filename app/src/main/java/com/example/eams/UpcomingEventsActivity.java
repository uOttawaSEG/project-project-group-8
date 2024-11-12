package com.example.eams;

import android.app.Activity;
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

public class UpcomingEventsActivity extends AppCompatActivity {

    private ListView upcomingEventsListView;
    private DatabaseReference eventsRef;
    private List<String> upcomingEventsList;
    private ArrayAdapter<String> adapter;
    private Intent receive;
    private String organizerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        upcomingEventsListView = findViewById(R.id.upcomingEventsListView);
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        receive = getIntent();

        organizerEmail = receive.getStringExtra("organizerEmail");

        upcomingEventsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, upcomingEventsList);
        upcomingEventsListView.setAdapter(adapter);

        loadUpcomingEvents(organizerEmail);

        upcomingEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(UpcomingEventsActivity.this, EventRegistrationRequestActivity.class);
                intent.putExtra("EventInfo", item);
                intent.putExtra("organizerEmail",organizerEmail);
                startActivity(intent);
                finish();
            }

        });

    }

    private void loadUpcomingEvents(String organizerEmail) {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                upcomingEventsList.clear();
                long currentTime = System.currentTimeMillis();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    EventInfo event = eventSnapshot.getValue(EventInfo.class);
                    if (event != null && isUpcomingEvent(event.getStartTime(), event.getDate()) && organizerEmail.equals(event.getEmail())) {
                        String eventDate = dateFormat.format(new Date(event.getDate()));
                        String startTime = timeFormat.format(new Date(event.getStartTime()));
                        String endTime = timeFormat.format(new Date(event.getEndTime()));

                        List<String> attendees = event.getAttendees();
                        String attendeesList = event.getAttendees() != null && !event.getAttendees().isEmpty()
                                ? String.join(", ", event.getAttendees())
                                : "No attendees";


                        String eventDetails = "Title: " + event.getTitle() +
                                "\nDate: " + eventDate +
                                "\nStart Time: " + startTime +
                                "\nEnd Time: " + endTime +
                                "\nAddress: " + event.getAddress() +
                                "\nDescription: " + event.getDescription() +
                                "\nAttendees: " + attendeesList;
                        upcomingEventsList.add(eventDetails);

                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpcomingEventsActivity.this, "Failed to load upcoming events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isUpcomingEvent(long eventStartTimeInMillis, long eventDate) {
        long currentTimeInMillis = System.currentTimeMillis();
        boolean isUpcoming = (eventStartTimeInMillis > currentTimeInMillis) || (eventDate > currentTimeInMillis);
        return isUpcoming;
    }

}

