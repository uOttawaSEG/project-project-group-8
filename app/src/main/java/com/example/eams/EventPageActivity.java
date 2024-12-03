package com.example.eams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
            title = str.substring(7, i).trim();
        }
    }

    // method to delete event
    public void delete(View view) {
        eventReference = FirebaseDatabase.getInstance().getReference("events");
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    EventInfo event = d.getValue(EventInfo.class);

                    if (event != null && title != null && event.getTitle().trim().equalsIgnoreCase(title.trim())) {

                        if (event.getAttendees() != null && !event.getAttendees().isEmpty()) {
                            Toast.makeText(EventPageActivity.this, "This event cannot be deleted as it has registered attendees.", Toast.LENGTH_LONG).show();
                            return;
                        }


                        d.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(EventPageActivity.this, "Event deleted successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(EventPageActivity.this, "Failed to delete event.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }

                // If event not found
                Toast.makeText(EventPageActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventPageActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
