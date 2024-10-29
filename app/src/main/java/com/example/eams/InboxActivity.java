package com.example.eams;

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

public class InboxActivity extends AppCompatActivity {

    private DatabaseReference attendeeReference;
    private DatabaseReference organizerReference;

    private ListView requestListView;
    private List<String> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        attendeeReference = FirebaseDatabase.getInstance().getReference("attendees");
        organizerReference = FirebaseDatabase.getInstance().getReference("organizers");


        requestList = new ArrayList<>();
        requestListView = findViewById(R.id.requestListView);

        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {

                    if (attendeeSnapshot.getValue() == null) {
                        Toast.makeText(InboxActivity.this, "No Data Available", Toast.LENGTH_LONG).show();
                    }
                    String request;
                    Attendee attendee = attendeeSnapshot.getValue(Attendee.class);
                    request = "Name: "+attendee.getFirstName()+" "+attendee.getLastName()+" Email: "+attendee.getEmail()+" Phone:"+attendee.getPhoneNumber()+" Address: "+attendee.getAddress();
                    requestList.add(request);

                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,
                        requestList);
                requestListView.setAdapter(adapter);
                requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InboxActivity.this, "Error fetching requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        attendeeReference.addValueEventListener(requestListener);

        ValueEventListener requestListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot organizerSnapshot : dataSnapshot.getChildren()) {

                    if (organizerSnapshot.getValue() == null) {
                        Toast.makeText(InboxActivity.this, "No Data Available", Toast.LENGTH_LONG).show();
                    }

                    String request;
                    Organizer organizer = organizerSnapshot.getValue(Organizer.class);
                    request = "Name: "+organizer.getFirstName()+" "+organizer.getLastName()+" Email: "+organizer.getEmail()+" Phone: "+organizer.getPhoneNumber()+ " Organization Name: "+organizer.getOrganizationName()+" Address: "+ organizer.getAddress();
                    requestList.add(request);

                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,
                        requestList);
                requestListView.setAdapter(adapter);
                requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InboxActivity.this, "Error fetching requests: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        organizerReference.addValueEventListener(requestListener2);

    }
}
