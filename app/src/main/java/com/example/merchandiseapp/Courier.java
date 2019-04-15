package com.example.merchandiseapp;

import java.util.HashMap;

public class Courier {
    private String email;
    private Integer Contact;
    private String Organisation;
    private Integer AccessLevel;
    private String Address;

    public Courier(String email, Integer contact, String organisation, Integer accessLevel, String address) {
        this.email = email;
        Contact = contact;
        Organisation = organisation;
        AccessLevel = accessLevel;
        Address = address;
    }

    public Courier()
    {

    }


    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("Email", email) ;
        result.put("Contact", Contact) ;
        result.put("Organisation", Organisation) ;
        result.put("AccessLevel", AccessLevel) ;
        result.put("Address", Address) ;

        return result ;

    }


}
