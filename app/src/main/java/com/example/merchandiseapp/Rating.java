package com.example.merchandiseapp;

public class Rating {
    private String email;
    private Integer stars;
    private String comment;

    public Rating(String email, Integer stars, String comment) {
        this.email = email;
        this.stars = stars;
        this.comment = comment;
    }
}
