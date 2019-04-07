package com.example.merchandiseapp;

import android.content.Intent;

import java.util.HashMap;

public class Merchandise {
      String BrandName ;
      String Image ;
      String ManuAddress ;
      String Material;
      String ProdID;
      boolean ReturnApplicable;
      String Type;
      String VendorID;
      Intent price;

    public Merchandise(String brandName, String image, String manuAddress, String material,
                       String prodID, boolean returnApplicable, String type, String vendorID, Intent price) {
        BrandName = brandName;
        Image = image;
        ManuAddress = manuAddress;
        Material = material;
        ProdID = prodID;
        ReturnApplicable = returnApplicable;
        Type = type;
        VendorID = vendorID;
        this.price = price;
    }

    public Merchandise()
    {

    }



    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("BrandName", BrandName) ;
        result.put("Image", Image) ;
        result.put("ManuAddress", ManuAddress) ;
        result.put("Material", Material) ;
        result.put("ProdID", ProdID) ;
        result.put("ReturnApplicable", ReturnApplicable) ;
        result.put("Type", Type) ;
        result.put("VendorId", VendorID) ;
        result.put("price", price) ;

        return result ;

    }





}
