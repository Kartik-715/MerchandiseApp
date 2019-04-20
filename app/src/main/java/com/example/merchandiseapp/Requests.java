package com.example.merchandiseapp;

public class Requests
{
    private String Address;
    private String Contact;
    private String Date;
    private String Email;
    private String GroupName;
    private String IsPaid;
    private String IsPlaced;
    private String OrderID;
    private String Quantity;
    private String Size;
    private String Status;
    private String Time;
    private String UserID;
    private String UserName;
    private String Image;
    private String ProductID;
    private String Price;

    public Requests()
    {
    }

    public Requests(String address, String contact, String date, String email, String groupName, String isPaid, String isPlaced, String orderID, String quantity, String size, String status, String time, String userID, String userName, String image, String productID, String price)
    {
        Address = address;
        Contact = contact;
        Date = date;
        Email = email;
        GroupName = groupName;
        IsPaid = isPaid;
        IsPlaced = isPlaced;
        OrderID = orderID;
        Quantity = quantity;
        Size = size;
        Status = status;
        Time = time;
        UserID = userID;
        UserName = userName;
        Image = image;
        ProductID = productID;
        Price = price;
    }

    public String getPrice()
    {
        return Price;
    }

    public void setPrice(String price)
    {
        Price = price;
    }

    public String getAddress()
    {
        return Address;
    }

    public void setAddress(String address)
    {
        Address = address;
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

    public String getIsPaid()
    {
        return IsPaid;
    }

    public void setIsPaid(String isPaid)
    {
        IsPaid = isPaid;
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

    public String getUserName()
    {
        return UserName;
    }

    public void setUserName(String userName)
    {
        UserName = userName;
    }

    public String getImage()
    {
        return Image;
    }

    public void setImage(String image)
    {
        Image = image;
    }

    public String getProductID()
    {
        return ProductID;
    }

    public void setProductID(String productID)
    {
        ProductID = productID;
    }
}
