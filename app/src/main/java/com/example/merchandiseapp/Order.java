package com.example.merchandiseapp;

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
}
