package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventPageActivity extends AppCompatActivity {

    private String str, title, find;
    private DatabaseReference eventReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        Intent receive = getIntent();
        str = receive.getStringExtra("EventInfo");

        TextView textView = findViewById(R.id.textView);
        textView.setText(str);
        find = "Date:";
        int i = str.indexOf(find);
        if (i > 0) {
            title = str.substring(7, i);
            title = title.trim();
        }
    }
    public void registerAttendee(String attendeeName) {
        eventReference = FirebaseDatabase.getInstance().getReference("events");
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    EventInfo event = d.getValue(EventInfo.class);
                    if (event != null && event.getTitle().equals(title)) {
                        // Add attendee to the event's attendees list
                        event.addAttendee(attendeeName);

                        d.getRef().setValue(event);
                        Toast.makeText(EventPageActivity.this, attendeeName + " registered successfully!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Method to handle deleting the event
    public void delete(View view) {
        eventReference = FirebaseDatabase.getInstance().getReference("events");
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean canDelete = true;
                DatabaseReference eventToDeleteRef = null;

                for (DataSnapshot d : snapshot.getChildren()) {
                    EventInfo event = d.getValue(EventInfo.class);

                    if (event != null && event.getTitle() != null) {
                        if (title != null && event.getTitle().trim().equalsIgnoreCase(title.trim())) {
                            if (event.getAttendees() != null && !event.getAttendees().isEmpty()) {
                                canDelete = false; // Event cant be deleted
                                break;
                            }
                            eventToDeleteRef = d.getRef();
                        }
                    }
                }

                if (canDelete && eventToDeleteRef != null) {
                    eventToDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EventPageActivity.this, "Delete successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EventPageActivity.this, "Delete unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (!canDelete) {
                    Toast.makeText(EventPageActivity.this, "Event cannot be deleted as it has registered attendees.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EventPageActivity.this, "Event not found for deletion.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventPageActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
