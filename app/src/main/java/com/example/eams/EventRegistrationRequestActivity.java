package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventRegistrationRequestActivity extends AppCompatActivity {

    private String eventInfo;
    private String title;
    private ListView registrationRequestListView;
    private DatabaseReference eventsRef;
    private List<String> registrationRequestList;
    private ArrayAdapter<String> adapter;
    private String organizerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration_request);

        Intent receive = getIntent();

        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventInfo = receive.getStringExtra("EventInfo");

        organizerEmail = receive.getStringExtra("organizerEmail");

        Pattern pattern = Pattern.compile("Title: (.*?)\\n");
        Matcher matcher = pattern.matcher(eventInfo);

        if (matcher.find()) {
            title = matcher.group(1).trim();
        }

        registrationRequestListView = findViewById(R.id.RegistrationRequestListView);
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        registrationRequestList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, registrationRequestList);
        registrationRequestListView.setAdapter(adapter);

        registrationRequestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(EventRegistrationRequestActivity.this, EventRegistrationRequestAttendeeDetailActivity.class);
                intent.putExtra("EventTitle", title);
                intent.putExtra("attendeeEmail", item);
                intent.putExtra("organizerEmail",organizerEmail);
                startActivity(intent);
                finish();
            }
        });

        registrationRequests();


    }

    private void registrationRequests() {

        Query query = eventsRef.orderByChild("title").equalTo(title);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                registrationRequestList.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                        String id = eventSnapshot.getKey();
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);

                        List<String> requestList = new ArrayList<>();

                        if(event.getRegistrationRequests() != null) {

                            requestList = event.getRegistrationRequests();
                        }



                        if(!requestList.isEmpty()) {

                            for(String request: requestList) {

                                registrationRequestList.add(request);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(EventRegistrationRequestActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventRegistrationRequestActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void approveAll(View view) {

        Query query = eventsRef.orderByChild("title").equalTo(title);

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

                            List<String> attendeesList = new ArrayList<>();

                            if(event.getAttendees() != null) {

                                attendeesList = event.getAttendees();
                            }

                            attendeesList.addAll(registrationRequests);

                            registrationRequests = new ArrayList<>();

                            event.setAttendees(attendeesList);

                            event.setRegistrationRequests(registrationRequests);

                            eventsRef.child(id).removeValue();

                            event.saveEventToDatabase();

                            Toast.makeText(EventRegistrationRequestActivity.this, "Approved All Requests Successfully", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                } else {
                    Toast.makeText(EventRegistrationRequestActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventRegistrationRequestActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteEvent(View view) {

        Query query2 = eventsRef.orderByChild("title").equalTo(title);

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                        String id = eventSnapshot.getKey();
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);

                        eventsRef.child(id).removeValue();

                        Toast.makeText(EventRegistrationRequestActivity.this, "Event successfully removed", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                } else {
                    Toast.makeText(EventRegistrationRequestActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventRegistrationRequestActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}