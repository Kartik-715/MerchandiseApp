package com.example.merchandiseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GiveRatingsActivity extends AppCompatActivity
{
    private RatingBar reviewRating;
    private EditText reviewsText;
    private Button reviewBtn;
    private DatabaseReference ratingRef, ratingRef2;
    private String Category, ProductID, GroupName;
    private String ratingValue;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_ratings);

        Category = getIntent().getStringExtra("category");
        ProductID = getIntent().getStringExtra("productID");
        GroupName = getIntent().getStringExtra("groupName");

        reviewBtn = findViewById(R.id.reviewClick);
        reviewRating = findViewById(R.id.reviewsBar);
        reviewsText = findViewById(R.id.reviews);
        radioGroup = findViewById(R.id.radiogroup);

        reviewRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                ratingValue = Float.toString(rating);
            }
        });



        reviewBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Btn_Click();
            }
        });
    }


    private void Btn_Click()
    {
        ratingRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(Category).child(ProductID).child("Rating").child(Prevalent.currentOnlineUser);
        int selectedID = radioGroup.getCheckedRadioButtonId();
        radioButton = ((RadioButton) findViewById(selectedID));

        System.out.println("Fuck : " + Prevalent.currentOnlineUser + " " + Prevalent.currentEmail + " " + GroupName);

        String comment = reviewsText.getText().toString();
        ratingRef.child("Comment").setValue(comment);
        ratingRef.child("Email").setValue(Prevalent.currentEmail);
        ratingRef.child("Group").setValue(GroupName);
        ratingRef.child("PID").setValue(ProductID);
        ratingRef.child("Stars").setValue(ratingValue);
        ratingRef.child("UID").setValue(Prevalent.currentOnlineUser);
        ratingRef.child("Category").setValue(Category);

        if(radioButton.getText().toString().equals("Public Review"))
            ratingRef.child("IsPrivate").setValue("No");
        else
            ratingRef.child("IsPrivate").setValue("Yes");

        ratingRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupName).child("Merchandise").child(Category).child(ProductID).child("Rating").child(Prevalent.currentOnlineUser);

        ratingRef2.child("Comment").setValue(comment);
        ratingRef2.child("Email").setValue(Prevalent.currentEmail);
        ratingRef2.child("Group").setValue(GroupName);
        ratingRef2.child("PID").setValue(ProductID);
        ratingRef2.child("Stars").setValue(ratingValue);
        ratingRef2.child("UID").setValue(Prevalent.currentOnlineUser);
        ratingRef2.child("Category").setValue(Category);

        if(radioButton.getText().toString().equals("Public Review"))
            ratingRef2.child("IsPrivate").setValue("No");
        else
            ratingRef2.child("IsPrivate").setValue("Yes");

        Intent intent = new Intent(GiveRatingsActivity.this, DeliveredActivity.class);
        startActivity(intent);
    }

}
