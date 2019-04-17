package com.example.merchandiseapp;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.HashMap;

public class Merchandise
{
    private ArrayList<String> AccessGroup;
    private String Category;
    private String GroupName;
    private ArrayList<String> Image;
    private String Material;
    private String OrderType;
    private String PID;
    private String Price;
    private ArrayList<String> Quantity;
    private ArrayList<String> Size;


    public Merchandise()
    {

    }

    public Merchandise(ArrayList<String> accessGroup, String category, String groupName, ArrayList<String> image, String material, String orderType, String PID, String price, ArrayList<String> quantity, ArrayList<String> size)
    {
        AccessGroup = accessGroup;
        Category = category;
        GroupName = groupName;
        Image = image;
        Material = material;
        OrderType = orderType;
        this.PID = PID;
        Price = price;
        Quantity = quantity;
        Size = size;
    }

    public ArrayList<String> getAccessGroup()
    {
        return AccessGroup;
    }

    public void setAccessGroup(ArrayList<String> accessGroup)
    {
        AccessGroup = accessGroup;
    }

    public String getCategory()
    {
        return Category;
    }

    public void setCategory(String category)
    {
        Category = category;
    }

    public String getGroupName()
    {
        return GroupName;
    }

    public void setGroupName(String groupName)
    {
        GroupName = groupName;
    }

    public ArrayList<String> getImage()
    {
        return Image;
    }

    public void setImage(ArrayList<String> image)
    {
        Image = image;
    }

    public String getMaterial()
    {
        return Material;
    }

    public void setMaterial(String material)
    {
        Material = material;
    }

    public String getOrderType()
    {
        return OrderType;
    }

    public void setOrderType(String orderType)
    {
        OrderType = orderType;
    }

    public String getPID()
    {
        return PID;
    }

    public void setPID(String PID)
    {
        this.PID = PID;
    }

    public String getPrice()
    {
        return Price;
    }

    public void setPrice(String price)
    {
        Price = price;
    }

    public ArrayList<String> getQuantity()
    {
        return Quantity;
    }

    public void setQuantity(ArrayList<String> quantity)
    {
        Quantity = quantity;
    }

    public ArrayList<String> getSize()
    {
        return Size;
    }

    public void setSize(ArrayList<String> size)
    {
        Size = size;
    }
}
