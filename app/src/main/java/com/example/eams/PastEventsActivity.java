package com.example.eams;

import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PastEventsActivity extends AppCompatActivity {

    private ListView pastEventsListView;
    private DatabaseReference eventsRef;
    private List<String> pastEventsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_events);

        pastEventsListView = findViewById(R.id.pastEventsListView);
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        pastEventsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pastEventsList);
        pastEventsListView.setAdapter(adapter);

        loadPastEvents();
    }

    private void loadPastEvents() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pastEventsList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    try {
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);
                        if (event != null) {
                            // Log for debugging
                            Log.d("PastEventsActivity", "Event title: " + event.getTitle());
                            Log.d("PastEventsActivity", "Date (long): " + event.getDate());
                            Log.d("PastEventsActivity", "Start Time (long): " + event.getStartTime());
                            Log.d("PastEventsActivity", "End Time (long): " + event.getEndTime());

                            // Check if the event date is in the past
                            if (isPastEvent(event.getDate())) {
                                String eventDetails = "Title: " + event.getTitle() +
                                        "\nDate: " + new Date(event.getDate()).toString() +
                                        "\nStart Time: " + new Date(event.getStartTime()).toString() +
                                        "\nEnd Time: " + new Date(event.getEndTime()).toString() +
                                        "\nAddress: " + event.getAddress() +
                                        "\nDescription: " + event.getDescription();
                                pastEventsList.add(eventDetails);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("PastEventsActivity", "Error processing event data", e);
                        Toast.makeText(PastEventsActivity.this, "Error loading event data", Toast.LENGTH_SHORT).show();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PastEventsActivity.this, "Failed to load past events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isPastEvent(long eventDate) {
        return eventDate < System.currentTimeMillis();
    }
}

