package com.example.merchandiseapp;

public class UserNode {
    public String Name;
    public String Address;
    public String Gender;
    public String Contact;
    public String EmailID;
    public String UID;

    public UserNode(String name, String address, String gender, String contact, String emailID, String uid) {
        Name = name;
        Address = address;
        Gender = gender;
        Contact = contact;
        EmailID = emailID;
        this.UID = uid;
    }
}

