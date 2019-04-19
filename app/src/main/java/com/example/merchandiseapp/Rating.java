package com.example.merchandiseapp;

import java.util.HashMap;

public class Rating {
    private String email;
    private Integer stars;
    private String comment;

    public Rating(String email, Integer stars, String comment) {
        this.email = email;
        this.stars = stars;
        this.comment = comment;
    }
    public Rating(){

    }
    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("email", email) ;
        result.put("stars", stars) ;
        result.put("comment", comment) ;

        return result ;

    }
}
