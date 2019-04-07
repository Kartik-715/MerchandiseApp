package com.example.merchandiseapp;


import java.util.HashMap;

public class User {
    private String email;
    private Integer Contact;
    private Double WalletMoney;
    private Integer AccessLevel;
    private String HomeAddress;

    public User(String email, Integer contact, Double walletMoney, Integer accessLevel, String homeAddress) {
        this.email = email;
        Contact = contact;
        WalletMoney = walletMoney;
        AccessLevel = accessLevel;
        HomeAddress = homeAddress;
    }
    public User()
    {

    }
    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("email", email) ;
        result.put("Contact", Contact) ;
        result.put("WalletMoney", WalletMoney) ;
        result.put("AccessLevel", AccessLevel) ;
        result.put("HomeAddress", HomeAddress) ;

        return result ;

    }
}
