package com.example.merchandiseapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class productDetailActivity extends AppCompatActivity {

private FloatingActionButton addToCart;

    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice , productName;
    private String bName;
    private String imgURL;
    private Long price;
    private String productID = "";
    private String productCategory = "";
    private String User_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Merchandise obj = (Merchandise) getIntent().getParcelableExtra("merchandiseObj");

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = findViewById(R.id.numberBtn);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        productName.setText(obj.getBrandName());
        productPrice.setText(obj.getPrice()[0].toString());
        productID = obj.getProdID();
        productCategory = obj.getCategory();
        User_ID = Prevalent.currentOnlineUser.toString();

        Picasso.get().load(obj.getImage()).into(productImage);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount ","");
        cartMap.put("isplaced", "false");

        cartListRef.child(User_ID).child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(productDetailActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(productDetailActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }
                    }
                });
    }
}
