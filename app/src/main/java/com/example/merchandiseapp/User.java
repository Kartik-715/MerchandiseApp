package com.example.merchandiseapp;


public class User {
    public String AccessLevel;
    public String Address;
    public String Contact;
    public String EmailID;
    public String Gender;
    public String Name;
    public String WalletMoney;


    public User(){

    }

    public User(String accessLevel, String address, String contact, String emailID, String gender, String name, String walletMoney) {
        AccessLevel = accessLevel;
        Address = address;
        Contact = contact;
        EmailID = emailID;
        Gender = gender;
        Name = name;
        WalletMoney = walletMoney;
    }


}
