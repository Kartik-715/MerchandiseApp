package com.example.merchandiseapp;

public class User_data {
    public String Name;
    public String Contact;
    public String Password;
    public String EmailID;
    public String UPI;
    public String AccessLevel;
    public String Address;

    public User_data(){

    }

    public User_data(String username, String contact, String password, String emailID, String UPI, String accessLevel, String address) {
        Name = username;
        Contact = contact;
        Password = password;
        EmailID = emailID;
        this.UPI = UPI;
        AccessLevel = accessLevel;
        Address = address;
    }

}
