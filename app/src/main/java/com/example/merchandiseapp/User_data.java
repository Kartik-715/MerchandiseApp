package com.example.merchandiseapp;

public class User_data {
    public String Name;
    public String Contact;
    public String Password;
    public String EmailID;
    public String UPI;
    public String AccessLevel;
    public String Address;
    public String Group;

    public User_data(String username_str, String contact_str, String password_str, String emailid_str, String upi_str, String s, String address_str){

    }

    public User_data(String name, String contact, String password, String emailID, String UPI, String accessLevel, String address, String group) {
        Name = name;
        Contact = contact;
        Password = password;
        EmailID = emailID;
        this.UPI = UPI;
        AccessLevel = accessLevel;
        Address = address;
        Group = group;
    }
}
