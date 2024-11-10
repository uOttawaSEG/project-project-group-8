package com.example.eams;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class EventInfo {
    private String title;
    private String description;
    private String address;
    private long date;
    private long startTime;
    private long endTime;
    private boolean autoApprove;
    private DatabaseReference databaseReference;
    private String email;

    public EventInfo() {
    }
    public EventInfo(String title, String description, String address, Calendar date, Calendar startTime, Calendar endTime, boolean autoApprove, String email) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.date = date.getTimeInMillis();
        this.startTime = startTime.getTimeInMillis();
        this.endTime = endTime.getTimeInMillis();
        this.autoApprove = autoApprove;
        this.email = email;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("events");
    }

    public void saveEventToDatabase() {
        String eventId = databaseReference.push().getKey();
        if (eventId != null) {
            databaseReference.child(eventId).setValue(this)
                    .addOnSuccessListener(aVoid -> {

                    })
                    .addOnFailureListener(e -> {

                    });
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public long getDate() {
        return date;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public String getEmail() {return email; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(Calendar date) {
        this.date = date.getTimeInMillis();
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime.getTimeInMillis();
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime.getTimeInMillis();
    }
    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public void setEmail(String email) {this.email = email; }
}
