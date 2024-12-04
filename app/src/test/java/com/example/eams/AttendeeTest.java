package com.example.eams;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttendeeTest {

    @Test
    public void testAttendeeRegistration() {
        Attendee attendee = new Attendee("John", "Doe", "john.doe@example.com", "password123", "1234567890", "123 Main St");

        // Validate the Attendee object
        assertEquals("John", attendee.getFirstName());
        assertEquals("Doe", attendee.getLastName());
        assertEquals("john.doe@example.com", attendee.getEmail());
        assertEquals("pending", attendee.getStatus()); // Default status should be 'pending'
    }

    @Test
    public void testAttendeeStatusUpdate() {
        Attendee attendee = new Attendee("John", "Doe", "john.doe@example.com", "password123", "1234567890", "123 Main St");
        attendee.setStatus("approved");

        // to check if status has been updated correctly
        assertEquals("approved", attendee.getStatus());
    }
}



