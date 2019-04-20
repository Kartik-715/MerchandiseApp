package com.example.merchandiseapp;

public class Notifications {
    private String Contact;
    private String Email;
    private String GroupName;
    private String  isApproved;
    private String Uid;

    public Notifications() {
    }

    public Notifications(String contact, String email, String groupName, String isApproved, String uid) {
        Contact = contact;
        Email = email;
        GroupName = groupName;
        this.isApproved = isApproved;
        Uid = uid;
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

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
