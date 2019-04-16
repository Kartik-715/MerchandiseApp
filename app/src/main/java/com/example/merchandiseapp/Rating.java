package com.example.merchandiseapp;

import java.util.HashMap;

public class Rating {
    private String email;
    private Integer stars;
    private String comment;
    private String uid;
    private String pid;

    public Rating(String email, Integer stars, String comment, String uid, String pid) {
        this.email = email;
        this.stars = stars;
        this.comment = comment;
        this.uid = uid;
        this.pid = pid;
    }

    public Rating() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
