package com.example.merchandiseapp;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.HashMap;

public class Merchandise
{
    private String BrandName ;
    private String Image ;
    private String ManuAddress ;
    private String Material;
    private String ProdID;
    private String ReturnApplicable;
    private String Category;
    private String VendorID;
    private ArrayList<String> price;
    private ArrayList<String> quantity;
    private String isMale ;

    public Merchandise()
    {

    }

    public Merchandise(String brandName, String image, String manuAddress, String material, String prodID, String returnApplicable, String category, String vendorID, ArrayList<String> price, ArrayList<String> quantity, String isMale)
    {
        BrandName = brandName;
        Image = image;
        ManuAddress = manuAddress;
        Material = material;
        ProdID = prodID;
        ReturnApplicable = returnApplicable;
        Category = category;
        VendorID = vendorID;
        this.price = price;
        this.quantity = quantity;
        this.isMale = isMale;
    }

    public String getBrandName()
    {
        return BrandName;
    }

    public void setBrandName(String brandName)
    {
        BrandName = brandName;
    }

    public String getImage()
    {
        return Image;
    }

    public void setImage(String image)
    {
        Image = image;
    }

    public String getManuAddress()
    {
        return ManuAddress;
    }

    public void setManuAddress(String manuAddress)
    {
        ManuAddress = manuAddress;
    }

    public String getMaterial()
    {
        return Material;
    }

    public void setMaterial(String material)
    {
        Material = material;
    }

    public String getProdID()
    {
        return ProdID;
    }

    public void setProdID(String prodID)
    {
        ProdID = prodID;
    }

    public String getReturnApplicable()
    {
        return ReturnApplicable;
    }

    public void setReturnApplicable(String returnApplicable)
    {
        ReturnApplicable = returnApplicable;
    }

    public String getCategory()
    {
        return Category;
    }

    public void setCategory(String category)
    {
        Category = category;
    }

    public String getVendorID()
    {
        return VendorID;
    }

    public void setVendorID(String vendorID)
    {
        VendorID = vendorID;
    }

    public ArrayList<String> getPrice()
    {
        return price;
    }

    public void setPrice(ArrayList<String> price)
    {
        this.price = price;
    }

    public ArrayList<String> getQuantity()
    {
        return quantity;
    }

    public void setQuantity(ArrayList<String> quantity)
    {
        this.quantity = quantity;
    }

    public String getIsMale()
    {
        return isMale;
    }

    public void setIsMale(String isMale)
    {
        this.isMale = isMale;
    }
}
