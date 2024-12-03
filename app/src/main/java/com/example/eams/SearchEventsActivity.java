package com.example.eams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;

public class SearchEventsActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ImageButton searchButton;
    private ListView attendeeEventsListView;
    private ArrayAdapter<String> eventsAdapter;
    private List<String> eventsList = new ArrayList<>();
    private DatabaseReference eventsRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_register);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        attendeeEventsListView = findViewById(R.id.AttendeeEventsListView);

        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsList);
        attendeeEventsListView.setAdapter(eventsAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEvents();
            }
        });

        attendeeEventsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEvent = eventsList.get(position);
            Toast.makeText(SearchEventsActivity.this, "Selected event: " + selectedEvent, Toast.LENGTH_SHORT).show();
        });
    }

    private void searchEvents() {
        String keyword = searchEditText.getText().toString().trim();

        if (!keyword.isEmpty()) {
            eventsRef.orderByChild("title").startAt(keyword).endAt(keyword + "\uf8ff")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            eventsList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                EventInfo event = snapshot.getValue(EventInfo.class);
                                if (event != null) {
                                    eventsList.add(event.getTitle());
                                }
                            }

                            if (eventsList.isEmpty()) {
                                Toast.makeText(SearchEventsActivity.this, "No events found for the keyword: " + keyword, Toast.LENGTH_SHORT).show();
                            } else {
                                eventsAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(SearchEventsActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter a keyword to search.", Toast.LENGTH_SHORT).show();
        }
    }
}
