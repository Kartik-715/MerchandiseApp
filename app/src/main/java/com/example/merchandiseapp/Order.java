package com.example.merchandiseapp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Order
{

    private String Address;
    private String Category;
    private String Contact;
    private String Date;
    private String Email;
    private String GroupName;
    private String IsPlaced;
    private String OrderID;
    private String Price;
    private String ProductID;
    private String Quantity;
    private String Size;
    private String Status;
    private ArrayList<String> Image;
    private String Time;
    private String UserID;

    public Order( ) {
    }

    public Order(String address, String category, String contact, String date, String email, String groupName, String isPlaced, String orderID, String price, String productID, String quantity, String size, String status, ArrayList<String> image, String time, String userID)
    {
        Address = address;
        Category = category;
        Contact = contact;
        Date = date;
        Email = email;
        GroupName = groupName;
        IsPlaced = isPlaced;
        OrderID = orderID;
        Price = price;
        ProductID = productID;
        Quantity = quantity;
        Size = size;
        Status = status;
        Image = image;
        Time = time;
        UserID = userID;
    }

    public String getAddress()
    {
        return Address;
    }

    public void setAddress(String address)
    {
        Address = address;
    }

    public String getCategory()
    {
        return Category;
    }

    public void setCategory(String category)
    {
        Category = category;
    }

    public String getContact()
    {
        return Contact;
    }

    public void setContact(String contact)
    {
        Contact = contact;
    }

    public String getDate()
    {
        return Date;
    }

    public void setDate(String date)
    {
        Date = date;
    }

    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String email)
    {
        Email = email;
    }

    public String getGroupName()
    {
        return GroupName;
    }

    public void setGroupName(String groupName)
    {
        GroupName = groupName;
    }

    public String getIsPlaced()
    {
        return IsPlaced;
    }

    public void setIsPlaced(String isPlaced)
    {
        IsPlaced = isPlaced;
    }

    public String getOrderID()
    {
        return OrderID;
    }

    public void setOrderID(String orderID)
    {
        OrderID = orderID;
    }

    public String getPrice()
    {
        return Price;
    }

    public void setPrice(String price)
    {
        Price = price;
    }

    public String getProductID()
    {
        return ProductID;
    }

    public void setProductID(String productID)
    {
        ProductID = productID;
    }

    public String getQuantity()
    {
        return Quantity;
    }

    public void setQuantity(String quantity)
    {
        Quantity = quantity;
    }

    public String getSize()
    {
        return Size;
    }

    public void setSize(String size)
    {
        Size = size;
    }

    public String getStatus()
    {
        return Status;
    }

    public void setStatus(String status)
    {
        Status = status;
    }

    public ArrayList<String> getImage()
    {
        return Image;
    }

    public void setImage(ArrayList<String> image)
    {
        Image = image;
    }

    public String getTime()
    {
        return Time;
    }

    public void setTime(String time)
    {
        Time = time;
    }

    public String getUserID()
    {
        return UserID;
    }

    public void setUserID(String userID)
    {
        UserID = userID;
    }
}