package com.example.merchandiseapp;

import java.util.HashMap;

public class Order
{
    private String buyer;
    private Merchandise item ;
    private Integer size ;
    private Integer quantity ;
    private boolean placed= false;

    public Order(String buyer, Merchandise item, Integer size, Integer quantity, boolean placed)
    {
        this.buyer = buyer;
        this.item = item;
        this.size = size;
        this.quantity = quantity;
        this.placed = placed;
    }

    public Order()
        {

    }
    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("buyer", buyer) ;
        result.put("item", item.toMap()) ;
        result.put("size", size) ;
        result.put("quantity", quantity) ;
        result.put("placed", placed) ;

        return result ;

    }
}
