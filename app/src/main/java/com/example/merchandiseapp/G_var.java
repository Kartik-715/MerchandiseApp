package com.example.merchandiseapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.microsoft.identity.client.PublicClientApplication;

public class G_var extends Application
{

    private String username;
    private String Email;
    private String uid;
    private String address;
    private String Contact;
    private String gender;
    private StorageReference imageRef;
    private Bitmap bitmap;
    private String UPI;
    private String ImageLocation;
    private PublicClientApplication sampleApp;



    public G_var() {
    }

    public PublicClientApplication getSampleApp()
    {
        return sampleApp;
    }

    public void setSampleApp(PublicClientApplication sampleApp)
    {
        this.sampleApp = sampleApp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public StorageReference getImageRef() {
        return imageRef;
    }

    public void setImageRef(StorageReference imageRef) {
        this.imageRef = imageRef;
    }

    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUPI() {
        return UPI;
    }

    public void setUPI(String UPI) {
        this.UPI = UPI;
    }

    public String getImageLocation() {
        return ImageLocation;
    }

    public void setImageLocation(String imageLocation) {
        ImageLocation = imageLocation;
    }
}
