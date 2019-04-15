package com.example.merchandiseapp;


import java.util.HashMap;

public class Merchandise {
      private String BrandName ;
      private String Image ;
      private String ManuAddress ;
      private String Material;
      private String ProdID;
      private boolean ReturnApplicable;
      private String Category;
      private String VendorID;
      private Integer price[];
      private Integer quantity[];
      private boolean isMale ;


    public Merchandise(String brandName, String image, String manuAddress, String material,
                       String prodID, boolean returnApplicable, String category, String vendorID, Integer[] Price, Integer[] Quantity, boolean _isMale) {
        BrandName = brandName;
        Image = image;
        ManuAddress = manuAddress;
        Material = material;
        ProdID = prodID;
        ReturnApplicable = returnApplicable;
        Category = category;
        VendorID = vendorID;
        price = new Integer[5] ;
        quantity = new Integer[5] ;
        price = Price.clone() ;
        quantity = Quantity.clone();
        isMale = _isMale ;

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
        result.put("Category", Category) ;
        result.put("VendorId", VendorID) ;
        result.put("Price", price) ;
        result.put("Quantity", quantity) ;
        result.put("isMale", isMale) ;

        return result ;

    }





}
