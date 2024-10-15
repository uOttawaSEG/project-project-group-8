package com.example.eams;

public class Organizer {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String organizationName;

    public Organizer(String fName, String lName, String mail, String pswrd, String phNumb, String adrs, String orgName) {
        firstName = fName;
        lastName = lName;
        email = mail;
        password = pswrd;
        phoneNumber = phNumb;
        address = adrs;
        organizationName = orgName;
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", organizationName="+ organizationName + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        firstName = fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        lastName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        email = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pswrd) {
        password = pswrd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phNumb) {
        phoneNumber = phNumb;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adrs) {
        address = adrs;
    }

    public String getOrganizationName() { 
        return organizationName;
    }

    public void setOrganizationName(String orgName) { 
        organizationName = orgName;
    }

    public String welcomeMessage() {
        return "Welcome! You are logged in as an Organizer.";
    }

}
