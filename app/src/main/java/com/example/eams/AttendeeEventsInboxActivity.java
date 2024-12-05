package com.example.eams;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eams.AttendeeEventRegisterActivity;
import com.example.eams.EventInfo;
import com.example.eams.R;
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

public class AttendeeEventsInboxActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ImageButton searchButton;
    private ListView upcomingEventsListView;
    private ArrayAdapter<String> adapter;
    private List<String> upcomingEventsList;
    private DatabaseReference eventsRef;
    private String attendeeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_events_inbox);

        attendeeEmail = getIntent().getStringExtra("email");

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        upcomingEventsListView = findViewById(R.id.AttendeeEventsListView);
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        upcomingEventsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, upcomingEventsList);
        upcomingEventsListView.setAdapter(adapter);

        upcomingEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(AttendeeEventsInboxActivity.this, AttendeeEventRegisterActivity.class);
                intent.putExtra("EventInfo", item);
                intent.putExtra("attendeeEmail", attendeeEmail);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEvents();
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    searchEvents();
                    return true;
                }
                return false;
            }
        });

        loadUpcomingEvents();
    }

    private void loadUpcomingEvents() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                upcomingEventsList.clear();
                long currentTime = System.currentTimeMillis();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    EventInfo event = eventSnapshot.getValue(EventInfo.class);
                    if (event != null && isUpcomingEvent(event.getStartTime(), event.getDate()) && (!alreadyRequested(event))) {
                        String eventDate = dateFormat.format(new Date(event.getDate()));
                        String startTime = timeFormat.format(new Date(event.getStartTime()));
                        String endTime = timeFormat.format(new Date(event.getEndTime()));


                        String eventDetails = "Title: " + event.getTitle() +
                                "\nDate: " + eventDate +
                                "\nStart Time: " + startTime +
                                "\nEnd Time: " + endTime +
                                "\nAddress: " + event.getAddress() +
                                "\nDescription: " + event.getDescription();
                        upcomingEventsList.add(eventDetails);

                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AttendeeEventsInboxActivity.this, "Failed to load upcoming events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchEvents() {
        String keyword = searchEditText.getText().toString().trim();

        if (!keyword.isEmpty()) {
            eventsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    upcomingEventsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        EventInfo event = snapshot.getValue(EventInfo.class);
                        if (event != null && (event.getTitle().toLowerCase().contains(keyword.toLowerCase()) || event.getDescription().toLowerCase().contains(keyword.toLowerCase())) && isUpcomingEvent(event.getStartTime(), event.getDate()) && (!alreadyRequested(event))) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                            String eventDate = dateFormat.format(new Date(event.getDate()));
                            String startTime = timeFormat.format(new Date(event.getStartTime()));
                            String endTime = timeFormat.format(new Date(event.getEndTime()));

                            String eventDetails = "Title: " + event.getTitle() +
                                    "\nDate: " + eventDate +
                                    "\nStart Time: " + startTime +
                                    "\nEnd Time: " + endTime +
                                    "\nAddress: " + event.getAddress() +
                                    "\nDescription: " + event.getDescription();
                            upcomingEventsList.add(eventDetails);
                        }
                    }

                    if (!upcomingEventsList.isEmpty()) {
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AttendeeEventsInboxActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            loadUpcomingEvents();
        }
    }

    private boolean isUpcomingEvent(long eventStartTimeInMillis, long eventDate) {
        long currentTimeInMillis = System.currentTimeMillis();
        boolean isUpcoming = (eventStartTimeInMillis > currentTimeInMillis);
        return isUpcoming;
    }

    private boolean alreadyRequested(EventInfo event) {

        if(event.getRegistrationRequests() != null) {

            if(event.getRegistrationRequests().contains(attendeeEmail))
                return true;
        }

        else if(event.getAttendees() != null) {

            if(event.getAttendees().contains(attendeeEmail))
                return true;
        }
        else if(event.getRejectedRequests() != null) {

            if(event.getRejectedRequests().contains(attendeeEmail))
                return true;
        }

        return false;
    }
}
