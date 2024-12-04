package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AttendeeMyEventDetailActivity extends AppCompatActivity {

    private String eventInfo;
    private String attendeeEmail;
    private String title="";
    private String status="";
    private DatabaseReference eventReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_my_event_detail);

        eventInfo = getIntent().getStringExtra("EventInfo");
        attendeeEmail = getIntent().getStringExtra("attendeeEmail");

        eventReference = FirebaseDatabase.getInstance().getReference("events");

        TextView userInfo = findViewById(R.id.eventTextView);
        userInfo.setText(eventInfo);

        Pattern pattern = Pattern.compile("Title: (.*?)\\n");
        Matcher matcher = pattern.matcher(eventInfo);

        Pattern pattern2 = Pattern.compile("Status:\\s*(\\w+)");
        Matcher matcher2 = pattern2.matcher(eventInfo);

        if (matcher.find()) {
            title = matcher.group(1).trim();
        }
        if (matcher2.find()) {
            status = matcher2.group(1).trim();
        }
    }

    public void cancel(View view) {

        Query query = eventReference.orderByChild("title").equalTo(title);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                        String id = eventSnapshot.getKey();
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);

                        if(status.equals("Approved") && canCancel(event.getStartTime())) {

                            List<String> attendeeList = new ArrayList<>();
                            if(event.getAttendees() != null) {

                                attendeeList = event.getAttendees();
                                attendeeList.remove(attendeeEmail);
                                event.setAttendees(attendeeList);
                                eventReference.child(id).removeValue();
                                event.saveEventToDatabase();
                                Toast.makeText(AttendeeMyEventDetailActivity.this, "Registration Cancelled Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        else if(status.equals("Pending") && canCancel(event.getStartTime())) {

                            List<String> requestList = new ArrayList<>();
                            if(event.getRegistrationRequests() != null) {

                                requestList = event.getRegistrationRequests();
                                requestList.remove(attendeeEmail);
                                event.setRegistrationRequests(requestList);
                                eventReference.child(id).removeValue();
                                event.saveEventToDatabase();
                                Toast.makeText(AttendeeMyEventDetailActivity.this, "Registration Cancelled Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        else if(status.equals("Rejected")){
                            Toast.makeText(AttendeeMyEventDetailActivity.this, "Unable to cancel(status=rejected)", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(AttendeeMyEventDetailActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AttendeeMyEventDetailActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean canCancel(long startTime) {

        long currentTime = System.currentTimeMillis();

        if (startTime - currentTime <= 24 * 60 * 60 * 1000) {
            Toast.makeText(this, "Cannot cancel: Event starts within 24 hours.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
