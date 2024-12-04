package com.example.eams;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OrganizerTest {

    @Test
    public void testOrganizerCreation() {
        Organizer organizer = new Organizer("Ae", "test", "al@uottawa.com", "securepass", "0987654321", "456 Another St", "Tech Co");

        // Validate the Organizer object
        assertEquals("Ae", organizer.getFirstName());
        assertEquals("test", organizer.getLastName());
        assertEquals("al@uottawa.com", organizer.getEmail());
        assertEquals("Tech Co", organizer.getOrganizationName());
    }

    @Test
    public void testOrganizerStatusUpdate() {
        Organizer organizer = new Organizer("Alice", "Smith", "alice.smith@example.com", "securepass", "0987654321", "456 Another St", "Tech Co");

        // initial status should be 'pending'
        assertEquals("pending", organizer.getStatus());

        // update status to 'approved'
        organizer.setStatus("approved");
        assertEquals("approved", organizer.getStatus());

        // update status to 'rejected'
        organizer.setStatus("rejected");
        assertEquals("rejected", organizer.getStatus());
    }
}




