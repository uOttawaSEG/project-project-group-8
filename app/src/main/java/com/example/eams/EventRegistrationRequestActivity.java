package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventRegistrationRequestActivity extends AppCompatActivity {

    private String title;
    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration_request);

        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        title = getIntent().getStringExtra("EventInfo").split("\n")[0].replace("Title: ", "").trim();
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


                        if (event.getAttendees() != null && !event.getAttendees().isEmpty()) {
                            Toast.makeText(EventRegistrationRequestActivity.this, "This event cannot be deleted as it has registered attendees.", Toast.LENGTH_LONG).show();
                            return;
                        }


                        eventsRef.child(id).removeValue();
                        Toast.makeText(EventRegistrationRequestActivity.this, "Event successfully removed", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
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
