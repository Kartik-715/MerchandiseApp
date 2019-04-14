package com.example.merchandiseapp;

public class Vendor {
    public String Name;
    public String Contact;
    public String Password;
    public String EmailID;
    public String UPI;
    public String AccessLevel;
    public String Address;


    public Vendor(String username, String contact, String password, String emailID, String UPI, String accessLevel, String address) {
        Name = username;
        Contact = contact;
        Password = password;
        EmailID = emailID;
        this.UPI = UPI;
        AccessLevel = accessLevel;
        Address = address;
    }

    public Vendor() {

    }

    public String getUsername() {
        return Name;
    }

    public void setUsername(String username) {
        Name = username;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getUPI() {
        return UPI;
    }

    public void setUPI(String UPI) {
        this.UPI = UPI;
    }

    public String getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        AccessLevel = accessLevel;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
