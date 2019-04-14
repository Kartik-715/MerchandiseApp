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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class productDetailActivity extends AppCompatActivity
{

    private FloatingActionButton addToCart;

    private Button addToCartButton, buyNowButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productName;
    private String productID = "";
    private String User_ID = "";
    private String orderID = "";
    private String image = "";
    private ArrayList<String> orderid_list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productID = getIntent().getStringExtra("pid");
        orderID = getIntent().getStringExtra("order_id");
        image = getIntent().getStringExtra("image");
        User_ID = Prevalent.currentOnlineUser;
        orderid_list = new ArrayList<>();

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_Button);
        numberButton = findViewById(R.id.numberBtn);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        getProductDetails(productID);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });

        buyNowButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BuyNow();
            }
        });
    }

    private void BuyNow()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        if(orderID.equals("empty"))
        {
            orderID = saveCurrentDate + " " + saveCurrentTime;
        }

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("contact", "");
        cartMap.put("address", "");
        cartMap.put("email","");
        cartMap.put("isplaced","false");
        cartMap.put("status","incart");
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount ","");
        cartMap.put("uid", User_ID);
        cartMap.put("orderid", orderID);
        cartMap.put("image", image);

        cartListRef.child(User_ID).child(orderID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    orderid_list.add(orderID);
                    Intent intent = new Intent(productDetailActivity.this, DetailsActivity.class);
                    intent.putExtra("orderid_list", orderid_list);
                    startActivity(intent);
                }
            }
        });

    }
    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        if(orderID.equals("empty"))
        {
            orderID = saveCurrentDate + " " + saveCurrentTime;
        }
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders");


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("contact", "");
        cartMap.put("address", "");
        cartMap.put("email","");
        cartMap.put("isplaced","false");
        cartMap.put("status","incart");
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount ","");
        cartMap.put("uid", User_ID);
        cartMap.put("orderid", orderID);
        cartMap.put("image", image);


        cartListRef.child(User_ID).child(orderID).updateChildren(cartMap)
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


    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child("Footwear");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                    int final_quantity = Integer.parseInt(merchandises.getQuantity().get(0));
                    System.out.println(Integer.toString(final_quantity));
                    numberButton.setRange(1,final_quantity);
                    productName.setText(merchandises.getBrandName());
                    productPrice.setText(merchandises.getPrice().get(0));
                    Picasso.get().load(merchandises.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
