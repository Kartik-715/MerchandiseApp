package com.example.merchandiseapp;

import java.io.Serializable;

public class Request implements Serializable {

   private String Contact;
    private String Email;
    private String Quantity;
    private String Size;
    private String UserID;
    private String UserName;

    public Request (String contact, String email, String quantity, String size, String userID, String userName) {

        Contact = contact;
        Email = email;
        Quantity = quantity;
        Size = size;
        UserID = userID;
        UserName = userName;
    }

    public Request() {
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
