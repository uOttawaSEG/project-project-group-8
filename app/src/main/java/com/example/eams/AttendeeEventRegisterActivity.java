package com.example.eams;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AttendeeEventRegisterActivity extends AppCompatActivity {

    private String eventInfo;
    private String attendeeEmail;
    private String title="";
    private DatabaseReference eventReference;
    private List<EventInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_register);

        eventInfo = getIntent().getStringExtra("EventInfo");
        attendeeEmail = getIntent().getStringExtra("attendeeEmail");

        eventReference = FirebaseDatabase.getInstance().getReference("events");
        list = new ArrayList<EventInfo>();

        TextView userInfo = findViewById(R.id.eventTextView);
        userInfo.setText(eventInfo);

        Pattern pattern = Pattern.compile("Title: (.*?)\\n");
        Matcher matcher = pattern.matcher(eventInfo);

        if (matcher.find()) {
            title = matcher.group(1).trim();
        }
        getRegisteredEvents();
    }

    public void register(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm", Locale.getDefault());

        Query query = eventReference.orderByChild("title").equalTo(title);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                        String id = eventSnapshot.getKey();
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);
                        String eventDate = dateFormat.format(new Date(event.getDate()));
                        int startTime = Integer.parseInt(timeFormat.format(new Date(event.getStartTime())));
                        int endTime = Integer.parseInt(timeFormat.format(new Date(event.getEndTime())));
                        if(findTimeConflict(eventDate, startTime, endTime)){
                            Toast.makeText(AttendeeEventRegisterActivity.this, "Time Conflict Found. Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(event.isAutoApprove()) {
                            event.addAttendee(attendeeEmail);
                            event.saveEventToDatabase();
                            Toast.makeText(AttendeeEventRegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            event.addRegistrationRequest(attendeeEmail);
                            event.saveEventToDatabase();
                            Toast.makeText(AttendeeEventRegisterActivity.this, "Registration Request Sent", Toast.LENGTH_SHORT).show();
                        }

                        eventReference.child(id).removeValue();
                        finish();
                    }
                } else {
                    Toast.makeText(AttendeeEventRegisterActivity.this, "No event found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AttendeeEventRegisterActivity.this, "Query cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRegisteredEvents(){
        eventReference.orderByChild("startTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                if(dataSnapshot.exists()) {

                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);
                        if (event != null) {

                            List<String> attendees = event.getAttendees();
                            List<String> requestList = event.getRegistrationRequests();
                            List<String> rejectedList = event.getRejectedRequests();
                            if(attendees != null) {
                                if (attendees.contains(attendeeEmail)) {
                                list.add(event);
                                }
                            }
                            if(requestList != null) {
                                if (requestList.contains(attendeeEmail)) {
                                    list.add(event);
                                }
                            }
                            if(rejectedList != null) {
                                if (rejectedList.contains(attendeeEmail)) {
                                    list.add(event);
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public boolean findTimeConflict(String eventDate, int startTime, int endTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm", Locale.getDefault());
        for(int k = 0; k< list.size(); k++){
            String eventDate2 = dateFormat.format(new Date(list.get(k).getDate()));
            int startTime2 = Integer.parseInt(timeFormat.format(new Date(list.get(k).getStartTime())));
            int endTime2 = Integer.parseInt(timeFormat.format(new Date(list.get(k).getEndTime())));
            if(eventDate.equals(eventDate2)){
                if(startTime == startTime2 && endTime == endTime2){
                    return true;
                }
                else if(startTime > startTime2 && startTime < endTime2){
                    return true;
                }
                else if(endTime > startTime2 && endTime < endTime2){
                    return true;
                }
            }
        }

        return false;
    }
}
