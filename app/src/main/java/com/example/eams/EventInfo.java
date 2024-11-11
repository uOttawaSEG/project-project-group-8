package com.example.eams;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EventInfo {
    private String title;
    private String description;
    private String address;
    private long date;
    private long startTime;
    private long endTime;
    private boolean autoApprove;
    private String email;
    private List<String> attendees;

    public EventInfo() {
    }


    public EventInfo(String title, String description, String address, long date, long startTime, long endTime, boolean autoApprove, String email) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.autoApprove = autoApprove;
        this.email = email;
        this.attendees = new ArrayList<>();
    }

    public void saveEventToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(String attendee) {
        if (this.attendees == null) {
            this.attendees = new ArrayList<>();
        }
        this.attendees.add(attendee);
    }
}
