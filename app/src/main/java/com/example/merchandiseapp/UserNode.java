package com.example.merchandiseapp;

public class UserNode {
    public String Name;
    public String Address;
    public String Gender;
    public String Contact;
    public String Email_ID;
    public String UID;

    public UserNode(String name, String address, String gender, String contact, String emailID, String uid) {
        Name = name;
        Address = address;
        Gender = gender;
        Contact = contact;
        Email_ID = emailID;
        this.UID = uid;
    }
}

