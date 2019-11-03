package com.nsu499.nsuts;

public class RegisterUserClass {
    private String nsuId;
    private String Email;
    private String Contact;
    private String Balance;
    private String Due;
    private String Booked;
    private Boolean Booking;

    public RegisterUserClass() {
    }

    public RegisterUserClass(String nsuId, String email, String contact, String balance, String Due, Boolean booking, String booked) {
        this.nsuId = nsuId;
        Email = email;
        Contact = contact;
        Balance = balance;
        Due = Due ;
        Booking = booking;
        Booked = booked;
    }

    public String getNsuId() {
        return nsuId;
    }

    public void setNsuId(String nsuId) {
        this.nsuId = nsuId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getBalance() {
        return Balance;
    }

    public String getDue() {
        return Due;
    }

    public void setDue(String due) {
        Due = due;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public Boolean getBooking() {
        return Booking;
    }

    public void setBooking(Boolean booking) {
        Booking = booking;
    }

    public String getBooked() {
        return Booked;
    }

    public void setBooked(String booked) {
        Booked = booked;
    }
}
