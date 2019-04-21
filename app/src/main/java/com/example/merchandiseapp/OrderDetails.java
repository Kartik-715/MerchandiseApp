package com.example.merchandiseapp;

import java.util.ArrayList;

public class OrderDetails {

    private String Category;
    private String Image;
    private String PID;
    private String Price;
    private ArrayList<String> Quantity;
    private ArrayList<String> Size;

    public OrderDetails(String category, String image, String PID, String price, ArrayList<String> quantity, ArrayList<String> size) {
        Category = category;
        Image = image;
        this.PID = PID;
        Price = price;
        Quantity = quantity;
        Size = size;
    }

    public OrderDetails() {

    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public ArrayList<String> getQuantity() {
        return Quantity;
    }

    public void setQuantity(ArrayList<String> quantity) {
        Quantity = quantity;
    }

    public ArrayList<String> getSize() {
        return Size;
    }

    public void setSize(ArrayList<String> size) {
        Size = size;
    }
}
