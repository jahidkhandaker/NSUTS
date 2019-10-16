package com.nsu499.nsuts;

public class RegisterUserClass {
    private String nsuId;
    private String Email;
    private String Contact;
    private String Balance;
    private Boolean Booking;

    public RegisterUserClass() {
    }

    public RegisterUserClass(String nsuId, String email, String contact, String balance, Boolean booking) {
        this.nsuId = nsuId;
        Email = email;
        Contact = contact;
        Balance = balance;
        Booking = booking;
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

    public void setBalance(String balance) {
        Balance = balance;
    }

    public Boolean getBooking() {
        return Booking;
    }

    public void setBooking(Boolean booking) {
        Booking = booking;
    }
}
