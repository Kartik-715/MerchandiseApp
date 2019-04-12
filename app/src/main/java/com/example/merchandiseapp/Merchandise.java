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
    private Long price[];
    private Long quantity[];
    private boolean isMale ;


    public Merchandise(String brandName, String image, String manuAddress, String material,
                       String prodID, boolean returnApplicable, String category, String vendorID, Long[] Price, Long[] Quantity, boolean _isMale) {
        BrandName = brandName;
        Image = image;
        ManuAddress = manuAddress;
        Material = material;
        ProdID = prodID;
        ReturnApplicable = returnApplicable;
        Category = category;
        VendorID = vendorID;
        price = new Long[5] ;
        quantity = new Long[5] ;
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

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getManuAddress() {
        return ManuAddress;
    }

    public void setManuAddress(String manuAddress) {
        ManuAddress = manuAddress;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public boolean isReturnApplicable() {
        return ReturnApplicable;
    }

    public void setReturnApplicable(boolean returnApplicable) {
        ReturnApplicable = returnApplicable;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getVendorID() {
        return VendorID;
    }

    public void setVendorID(String vendorID) {
        VendorID = vendorID;
    }

    public Long[] getPrice() {
        return price;
    }

    public void setPrice(Long[] price) {
        this.price = price;
    }

    public Long[] getQuantity() {
        return quantity;
    }

    public void setQuantity(Long[] quantity) {
        this.quantity = quantity;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }
}
