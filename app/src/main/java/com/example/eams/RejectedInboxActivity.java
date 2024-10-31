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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class RejectedInboxActivity extends AppCompatActivity {

    private DatabaseReference attendeeReference;
    private DatabaseReference organizerReference;

    private ListView requestListView;
    private List<String> requestList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_inbox);

        attendeeReference = FirebaseDatabase.getInstance().getReference("attendees");
        organizerReference = FirebaseDatabase.getInstance().getReference("organizers");

        requestList = new ArrayList<>();
        requestListView = findViewById(R.id.requestListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestListView.setAdapter(adapter);

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                String userType = item.startsWith("Organizer:") ? "organizers" : "attendees";

                Intent intent = new Intent(RejectedInboxActivity.this, AdminFunctions.class);
                intent.putExtra("UserInfo", item);
                intent.putExtra("UserType", userType);
                startActivity(intent);
            }
        });

        loadAttendeeRequests();
        loadOrganizerRequests();
    }

    private void loadAttendeeRequests() {
        attendeeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();

                for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {
                    Attendee attendee = attendeeSnapshot.getValue(Attendee.class);
                    if (attendee != null && attendee.getStatus().equals("rejected")) {
                        String request = "Attendee: Name: " + attendee.getFirstName() + " " + attendee.getLastName() +
                                " | Email: " + attendee.getEmail() +
                                " | Phone: " + attendee.getPhoneNumber() +
                                " | Address: " + attendee.getAddress();
                        requestList.add(request);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RejectedInboxActivity.this, "Error fetching attendee requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrganizerRequests() {
        organizerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot organizerSnapshot : dataSnapshot.getChildren()) {
                    Organizer organizer = organizerSnapshot.getValue(Organizer.class);
                    if (organizer != null && organizer.getStatus().equals("rejected")) {
                        String request = "Organizer: Name: " + organizer.getFirstName() + " " + organizer.getLastName() +
                                " | Email: " + organizer.getEmail() +
                                " | Phone: " + organizer.getPhoneNumber() +
                                " | Organization: " + organizer.getOrganizationName() +
                                " | Address: " + organizer.getAddress();
                        requestList.add(request);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RejectedInboxActivity.this, "Error fetching organizer requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}