package com.example.merchandiseapp;

import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

public class productDetailActivity extends AppCompatActivity {

private FloatingActionButton addToCart;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice , productName;
    private String bName;
    private String imgURL;
    private Long price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Merchandise obj = (Merchandise) getIntent().getParcelableExtra("merchandiseObj");

        addToCart = findViewById(R.id.addToCart);
        numberButton = findViewById(R.id.numberBtn);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        productName.setText(obj.getBrandName());
        productPrice.setText(obj.getPrice()[0].toString());
        Picasso.get().load(obj.getImage()).into(productImage);


    }
}
