package com.example.merchandiseapp;

import java.util.HashMap;

public class Order
{
    private String buyer;
    private Merchandise item ;
    private String size ;
    private String quantity ;
    private String placed= "no";

    public Order()
    {

    }

    public Order(String buyer, Merchandise item, String size, String quantity, String placed)
    {
        this.buyer = buyer;
        this.item = item;
        this.size = size;
        this.quantity = quantity;
        this.placed = placed;
    }


    public String getBuyer()
    {
        return buyer;
    }

    public void setBuyer(String buyer)
    {
        this.buyer = buyer;
    }

    public Merchandise getItem()
    {
        return item;
    }

    public void setItem(Merchandise item)
    {
        this.item = item;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public String getPlaced()
    {
        return placed;
    }

    public void setPlaced(String placed)
    {
        this.placed = placed;
    }
}
