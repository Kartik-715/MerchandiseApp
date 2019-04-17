package com.example.merchandiseapp;
import android.os.Parcel;

import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;
import java.util.HashMap;

public class Merchandise
{
    private String GroupName ;
    private String Category ;
    private ArrayList<String> Image;
    private String Material;
    private String PID;
    private String Price;
    private ArrayList<String> Quantity;
    private ArrayList<String> Size;
    private ArrayList<String> AccessGroup;
    private String OrderType;

    public Merchandise()
    {

    }

    public Merchandise(String groupName, String category, ArrayList<String> image,
                       String material, String PID, String price, ArrayList<String> quantity, ArrayList<String> size,
                       ArrayList<String> accessGroup, String orderType) {
        GroupName = groupName;
        Category = category;
        Image = image;
        Material = material;
        this.PID = PID;
        Price = price;
        Quantity = quantity;
        Size = size;
        AccessGroup = accessGroup;
        OrderType = orderType;
    }


    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("GroupName", GroupName) ;
        result.put("Category", Category) ;
        result.put("Image", Image) ;
        result.put("Material",Material);
        result.put("PID",PID);
        result.put("Price",Price);
        result.put("Qunatity",Quantity);
        result.put("Size", Size);
        result.put("AccessGroup",AccessGroup);
        result.put("OrderType",OrderType);

        return result ;

    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public ArrayList<String> getImage() {
        return Image;
    }

    public void setImage(ArrayList<String> image) {
        Image = image;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
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

    public ArrayList<String> getAccessGroup() {
        return AccessGroup;
    }

    public void setAccessGroup(ArrayList<String> accessGroup) {
        AccessGroup = accessGroup;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }


}
