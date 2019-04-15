package com.example.merchandiseapp;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Order
{

    private String contact;
    private String address;
    private String date;
    private String email;
    private String orderid;
    private String uid;
    private String isplaced;
    private String pid;
    private String pname;
    private String price;
    private String quantity;
    private String status;
    private String time;
    private String image;
    private String category;
    private String size;

    public Order(String contact, String address, String date, String email, String orderid, String uid, String isplaced, String pid, String pname, String price, String quantity, String status, String time, String image, String category, String size)
    {
        this.contact = contact;
        this.address = address;
        this.date = date;
        this.email = email;
        this.orderid = orderid;
        this.uid = uid;
        this.isplaced = isplaced;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.time = time;
        this.image = image;
        this.category = category;
        this.size = size;
    }

    public Order()
    {
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getOrderid()
    {
        return orderid;
    }

    public void setOrderid(String orderid)
    {
        this.orderid = orderid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getIsplaced()
    {
        return isplaced;
    }

    public void setIsplaced(String isplaced)
    {
        this.isplaced = isplaced;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getPname()
    {
        return pname;
    }

    public void setPname(String pname)
    {
        this.pname = pname;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }
}