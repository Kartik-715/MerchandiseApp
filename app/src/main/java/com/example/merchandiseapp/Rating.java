package com.example.merchandiseapp;

import java.util.HashMap;

public class Rating {
    private String Email;
    private String Stars;
    private String Comment;
    private String UID;
    private String PID;

    public Rating() {
    }

    public Rating(String email, String stars, String comment, String UID, String PID) {
        Email = email;
        Stars = stars;
        Comment = comment;
        this.UID = UID;
        this.PID = PID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStars() {
        return Stars;
    }

    public void setStars(String stars) {
        Stars = stars;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }
}
