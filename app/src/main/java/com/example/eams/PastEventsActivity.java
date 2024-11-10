package com.example.eams;

import android.os.Bundle;
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

        String organizerEmail = getIntent().getStringExtra("organizerEmail");

        pastEventsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pastEventsList);
        pastEventsListView.setAdapter(adapter);

        loadPastEvents(organizerEmail);
    }

    private void loadPastEvents(String organizerEmail) {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pastEventsList.clear();
                long currentTime = System.currentTimeMillis();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    EventInfo event = eventSnapshot.getValue(EventInfo.class);
                    if (event != null && isPastEvent(event.getDate(), currentTime) && organizerEmail.equals(event.getEmail())) {
                        String eventDate = dateFormat.format(new Date(event.getDate()));
                        String startTime = timeFormat.format(new Date(event.getStartTime()));
                        String endTime = timeFormat.format(new Date(event.getEndTime()));

                        String eventDetails = "Title: " + event.getTitle() +
                                "\nDate: " + eventDate +
                                "\nStart Time: " + startTime +
                                "\nEnd Time: " + endTime +
                                "\nAddress: " + event.getAddress() +
                                "\nDescription: " + event.getDescription();
                        pastEventsList.add(eventDetails);
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

    private boolean isPastEvent(long eventDateInMillis, long currentTime) {
        boolean isPast = eventDateInMillis < currentTime;
        return isPast;
    }
}

