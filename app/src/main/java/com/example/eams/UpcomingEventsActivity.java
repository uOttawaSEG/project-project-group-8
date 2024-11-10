package com.example.eams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpcomingEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private List<EventInfo> eventsList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(eventsList);
        recyclerView.setAdapter(eventsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        fetchUpcomingEvents();
    }

    private void fetchUpcomingEvents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventInfo event = snapshot.getValue(EventInfo.class);
                    if (event != null && isUpcoming(event)) {
                        eventsList.add(event);
                        Log.d("Event Added", "Title: " + event.getTitle());
                    }
                }
                eventsAdapter.notifyDataSetChanged();
                Log.d("Event List Size", "Size: " + eventsList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpcomingEventsActivity.this, "Failed to load events: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isUpcoming(EventInfo event) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return event.getDate() > currentTime; // Compare long timestamps
    }
}
