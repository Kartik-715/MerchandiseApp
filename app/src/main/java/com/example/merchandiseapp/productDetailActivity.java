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

        addToCart = (FloatingActionButton) findViewById(R.id.addToCart);
        numberButton = (ElegantNumberButton) findViewById(R.id.numberBtn);
        productImage = (ImageView) findViewById(R.id.productImage);
        productName = (TextView) findViewById(R.id.productName);
        productPrice = (TextView) findViewById(R.id.productPrice);

        bName = getIntent().getStringExtra("brandName");
        imgURL = getIntent().getStringExtra("ImageUrl");

        price = getIntent().getLongExtra("price",0);
        System.out.println("HELLO1"+price);

        productName.setText(bName);
        productPrice.setText(price.toString());
        Picasso.get().load(imgURL).into(productImage);
    }
}
