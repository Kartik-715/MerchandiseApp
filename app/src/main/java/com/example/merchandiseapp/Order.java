package com.example.merchandiseapp;

import java.util.HashMap;

public class Order {
    private String userId;
    private Merchandise item ;
    private Integer size ;
    private Integer quantity ;
    private boolean placed= false;

    public Order(String userId, Merchandise item, Integer size, Integer quantity, boolean placed) {
        this.userId = userId;
        this.item = item;
        this.size = size;
        this.quantity = quantity;
        this.placed = placed;
    }

    public Order(){

    }
    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("userId", userId) ;
        result.put("item", item) ;
        result.put("size", size) ;
        result.put("quantity", quantity) ;
        result.put("placed", placed) ;

        return result ;

    }
}
