package com.example.eams;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class EventInfo {
    private String title;
    private String description;
    private String address;
    private Calendar date;
    private Calendar startTime;
    private Calendar endTime;
    private boolean autoApprove;
    private DatabaseReference databaseReference;
    private String email;

    public EventInfo(String title, String description, String address, Calendar date, Calendar startTime, Calendar endTime, boolean autoApprove, String email) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Calendar getDate() {
        return date;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
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
        this.date = date;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public void setEmail(String email) {this.email = email; }
}
