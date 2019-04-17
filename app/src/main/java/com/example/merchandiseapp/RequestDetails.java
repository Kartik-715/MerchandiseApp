package com.example.merchandiseapp;

public class RequestDetails {
    private String Category;
    private String Image;
    private String PID;
    private String Price;
    private String TotalQuantity;

    public RequestDetails(String category,
                          String image, String PID, String price, String totalQuantity) {
        Category = category;
        Image = image;
        this.PID = PID;
        Price = price;
        TotalQuantity = totalQuantity;
    }
    public RequestDetails()
    {

    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }
}
