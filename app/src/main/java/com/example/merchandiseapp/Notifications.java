package com.example.merchandiseapp;

public class Notifications {
    private String Contact;
    private String EmailID;
    private String GroupName;
    private String isApproved;
    private String UID;
    private String UPI;

    public Notifications() {
    }

    public Notifications(String contact, String email, String groupName, String isApproved, String uid) {
        Contact = contact;
        EmailID = email;
        GroupName = groupName;
        this.isApproved = isApproved;
        UID = uid;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }


    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getUPI() {
        return UPI;
    }

    public void setUPI(String UPI) {
        this.UPI = UPI;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }
}
