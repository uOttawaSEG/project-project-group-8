package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchEventsActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ImageButton searchButton;
    private ListView eventsListView;
    private ArrayAdapter<String> eventsAdapter;
    private List<String> eventsList = new ArrayList<>();
    private FirebaseFirestore db;
    private String attendeeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_search_events);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        eventsListView = findViewById(R.id.eventsListView);

        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsList);
        eventsListView.setAdapter(eventsAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");

        attendeeEmail = getIntent().getStringExtra("attendeeEmail");

        searchButton.setOnClickListener(v -> searchEvents());
        eventsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEvent = eventsList.get(position);
            Toast.makeText(this, "Selected event: " + selectedEvent, Toast.LENGTH_SHORT).show();
        });
    }

    private void searchEvents() {
        String keyword = searchEditText.getText().toString().trim();

        if (keyword.isEmpty()) {
            Toast.makeText(this, "Please enter a keyword to search.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("events")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Error retrieving events.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    eventsList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String eventTitle = document.getString("title");
                        String eventDescription = document.getString("description");

                        List<String> registeredEmails = (List<String>) document.get("registeredEmails");
                        List<String> pendingEmails = (List<String>) document.get("pendingEmails");

                        if ((registeredEmails != null && registeredEmails.contains(attendeeEmail)) ||
                                (pendingEmails != null && pendingEmails.contains(attendeeEmail))) {
                            continue;
                        }

                        if ((eventTitle != null && eventTitle.toLowerCase().contains(keyword.toLowerCase())) ||
                                (eventDescription != null && eventDescription.toLowerCase().contains(keyword.toLowerCase()))) {
                            eventsList.add(eventTitle);
                        }
                    }

                    eventsAdapter.notifyDataSetChanged();

                    if (eventsList.isEmpty()) {
                        Toast.makeText(this, "No events found matching the keyword.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
