package com.example.merchandiseapp;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Merchandise implements Parcelable {
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

    protected Merchandise(Parcel in) {


        BrandName = in.readString();
        Image  = in.readString();
        ManuAddress = in.readString();
        Material = in.readString();
        ProdID = in.readString();
        ReturnApplicable = in.readByte() != 0 ;
        Category = in.readString();
        VendorID = in.readString();

        Object[] price1 = in.readArray(getClass().getClassLoader());

        price = new Long[5];

        for(int i=0; i<price1.length; i++){
            price[i] = Long.parseLong(price1[i].toString());

            System.out.println(price1[i].toString());
        }

        Object[] qty1= in.readArray(getClass().getClassLoader());
        quantity = new Long[5];

        for(int i=0; i<qty1.length; i++){

            quantity[i]  = Long.parseLong(qty1[i].toString());
            System.out.println("hi1" + qty1[i].toString());
        }

        isMale = in.readByte() != 0;

    }

    public static final Creator<Merchandise> CREATOR = new Creator<Merchandise>() {
        @Override
        public Merchandise createFromParcel(Parcel in) {
            return new Merchandise(in);
        }

        @Override
        public Merchandise[] newArray(int size) {
            return new Merchandise[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    public static Creator<Merchandise> getCREATOR() {
        return CREATOR;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(BrandName);
        parcel.writeString(Image);
        parcel.writeString(ManuAddress);
        parcel.writeString(Material);
        parcel.writeString(ProdID);

        parcel.writeByte((byte) ( ReturnApplicable ? 1 : 0));
        parcel.writeString(Category);
        parcel.writeString(VendorID);
        parcel.writeArray((Long[]) price);
        parcel.writeArray((Long[]) quantity);
        
        parcel.writeByte((byte) ( isMale ? 1 : 0));

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
