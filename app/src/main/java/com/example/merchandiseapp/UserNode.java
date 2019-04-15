package com.example.merchandiseapp;

public class UserNode {
    public String Name;
    public String EmailID;
    public String AccessLevel;
    public String WalletMoney;

    public UserNode(String accessLevel, String name, String emailID, String walletMoney) {
        AccessLevel = accessLevel;
        Name = name;
        EmailID = emailID;
        WalletMoney = walletMoney;
    }
}

