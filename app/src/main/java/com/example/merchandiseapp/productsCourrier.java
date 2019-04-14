package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class productsCourrier extends AppCompatActivity {
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productName;
    private String productID = "";
    private String User_ID = "";
    private String orderID = "";
    private String date = "";
    private String time = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_courrier);

        Button  btn = (Button) findViewById(R.id.button1);

        productID = getIntent().getStringExtra("pid");
        date = getIntent().getStringExtra("pdate");
        time = getIntent().getStringExtra("ptime");

        User_ID = Prevalent.currentOnlineUser;
        System.out.println("\n123 "+ date);
        System.out.println("123 "+time);
        System.out.println("123 "+productID);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        getProductDetails(productID);

        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Packed and ready to be shipped"
                , "Shipped", "On Delivery" , "Delivered"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }

    private void getProductDetails(String productID)
    {
        final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("mayank343"
        );
         productsRef.child(date+" "+time).addValueEventListener(new ValueEventListener()
         {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@2"+date+" "+time+"@@@@@@@@@@@@@@@@@@@@");
                Order order = dataSnapshot.getValue(Order.class);
                //System.out.println("hello" + order);

               productName.setText(order.getPname());
               productPrice.setText(order.getPrice());



// Picasso.get().load(order.getStatus();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    public void doOnClick(View view)
    {





    }

}

