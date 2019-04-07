package com.example.merchandiseapp;


public class User {
    String email;
    Integer Contact;
    Double WalletMoney;
    Integer AccessLevel;
    String HomeAddress;

    public User(String email, Integer contact, Double walletMoney, Integer accessLevel, String homeAddress) {
        this.email = email;
        Contact = contact;
        WalletMoney = walletMoney;
        AccessLevel = accessLevel;
        HomeAddress = homeAddress;
    }
}
