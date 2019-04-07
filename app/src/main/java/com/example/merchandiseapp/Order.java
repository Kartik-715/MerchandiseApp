package com.example.merchandiseapp;

import java.util.HashMap;

public class Order {
    private String userId;
    private String vendorId;
    private double price;
    private String ProdID;
    private boolean placed= false;

    public Order(String userId, String vendorId, double price, String prodID, boolean placed) {
        this.userId = userId;
        this.vendorId = vendorId;
        this.price = price;
        ProdID = prodID;
        this.placed = placed;
    }
    public Order(){

    }
    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("userId", userId) ;
        result.put("vendorId", vendorId) ;
        result.put("price", price) ;
        result.put("ProdID", ProdID) ;
        result.put("placed", placed) ;

        return result ;

    }
}
