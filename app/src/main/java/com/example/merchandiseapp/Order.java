package com.example.merchandiseapp;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

public class Order
{
    private String Contact;
    private String Dilevery_Address;
    private String date;
    private String email;
    private String isplaced;
    private String pid;
    private String pname;
    private String price;
    private String quantity;
    private String status;
    private String time;



    public Order()
    {

    }

    public Order(String contact, String dilevery_Address, String date, String email,
                 String isplaced, String pid, String pname, String price, String quantity, String status, String time) {
        Contact = contact;
        Dilevery_Address = dilevery_Address;
        this.date = date;
        this.email = email;
        this.isplaced = isplaced;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.time = time;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getDilevery_Address() {
        return Dilevery_Address;
    }

    public void setDilevery_Address(String dilevery_Address) {
        Dilevery_Address = dilevery_Address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsplaced() {
        return isplaced;
    }

    public void setIsplaced(String isplaced) {
        this.isplaced = isplaced;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /* public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("buyer", buyer) ;
        result.put("item", item.toMap()) ;
        result.put("size", size) ;
        result.put("quantity", quantity) ;
        result.put("placed", placed) ;

        return result ;

    }*/

}
