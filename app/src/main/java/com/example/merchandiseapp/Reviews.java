package com.example.merchandiseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Reviews extends AppCompatActivity {
    private RatingBar reviewRating;
    private EditText reviewsText;
    private Button reviewBtn;
    private String vendorId;
    private String productId;
    private String reviewValue;
    private Float ratingValue;
    private DatabaseReference ratingRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews2);
        ratingRef = FirebaseDatabase.getInstance().getReference().child("Merchandise/Category/ProdID/Rating");
        reviewRating = findViewById(R.id.reviewsBar);
        reviewRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = rating;
                //Toast.makeText(Reviews.this, "" + String.valueOf(rating), Toast.LENGTH_SHORT).show();
            }
        });
        //User us = new
        final FirebaseAuth mauth = FirebaseAuth.getInstance();
        final FirebaseUser user = mauth.getCurrentUser();
        reviewsText = findViewById(R.id.reviews);
        reviewBtn = findViewById(R.id.reviewClick);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewValue = reviewsText.getText().toString();
                ratingRef.child(user.getUid()).child("comment").setValue(reviewValue);
                ratingRef.child(user.getUid()).child("email").setValue(user.getEmail());
                ratingRef.child(user.getUid()).child("stars").setValue(ratingValue);
                ratingRef.child(user.getUid()).child("uid").setValue(user.getUid());
                //ratingRef.child(user.getUid()).child("pid").setValue(.getProdID);
                //Toast.makeText(Reviews.this,""+reviewsText.getText(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
