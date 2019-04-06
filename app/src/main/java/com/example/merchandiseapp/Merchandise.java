package com.example.merchandiseapp;

import java.util.HashMap;

public class Merchandise {
      String BrandName ;
      String Image ;
      String ManuAddress ;


    public Merchandise()
    {

    }


    public Merchandise(String brandName, String image , String manuImage)
    {
        BrandName = brandName ;
        Image = image ;
        ManuAddress = manuImage ;
    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("BrandName", BrandName) ;
        result.put("Image", Image) ;
        result.put("ManuAddress", ManuAddress) ;
        return result ;

    }





}
