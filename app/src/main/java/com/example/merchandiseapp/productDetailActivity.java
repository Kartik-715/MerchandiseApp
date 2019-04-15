package com.example.merchandiseapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
    private String category = "";
    private ArrayList<String> orderid_list;
    private String image_src;
    private Spinner SizeSpinner;
    private String selecteditem;
    private ArrayList<String> arraySpinner;
    private int flag;

    public interface MyCallback
    {
        void onCallback(ArrayList<String> value);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productID = getIntent().getStringExtra("pid");
        orderID = getIntent().getStringExtra("order_id");
        image_src = getIntent().getStringExtra("image");
        category = getIntent().getStringExtra("category");
        User_ID = Prevalent.currentOnlineUser;
        orderid_list = new ArrayList<>();

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_Button);
        numberButton = findViewById(R.id.numberBtn);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        SizeSpinner = findViewById(R.id.size_spinner);
        productPrice = findViewById(R.id.productPrice);


        /*Spinner Display*/
        arraySpinner = new ArrayList<>();

        initializeSpinner(new MyCallback()
        {
            @Override
            public void onCallback(ArrayList<String> value)
            {
                System.out.println("qqqqqqqqqq" + arraySpinner);
                System.out.println("wwwwwwwwww" + value);

                if(arraySpinner.isEmpty())
                {
                    flag = 0;
                    getProductDetails(productID);
                    addToCartButton.setVisibility(View.INVISIBLE);
                    buyNowButton.setVisibility(View.INVISIBLE);
                    numberButton.setVisibility(View.INVISIBLE);
                    SizeSpinner.setVisibility(View.INVISIBLE);
                    productPrice.setVisibility(View.INVISIBLE);
                }

                else
                {
                    flag = 1;
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<>(productDetailActivity.this, android.R.layout.simple_list_item_1, arraySpinner);
                    myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SizeSpinner.setAdapter(myAdapter);

                    SizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            selecteditem = SizeSpinner.getSelectedItem().toString();
                            getProductDetails(productID);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });
                }

            }
        });

        productImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(productDetailActivity.this, FullPageImageActivity.class);
                intent.putExtra("image", image_src);
                startActivity(intent);
            }
        });


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

        String neworderID;

        if(orderID.equals("empty"))
        {
            neworderID = saveCurrentDate + " " + saveCurrentTime;
            orderID = neworderID;
        }

        else
        {
            neworderID = saveCurrentDate + " " + saveCurrentTime;
            orderID = orderID;

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
        cartMap.put("orderid", neworderID);
        cartMap.put("image", image);
        cartMap.put("category", category);
        cartMap.put("size", selecteditem);

        //Removing the previous one and making new one
        cartListRef.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                }
            }
        });

        cartListRef.child(User_ID).child(neworderID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
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
        String neworderID;

        if(orderID.equals("empty"))
        {
            neworderID = saveCurrentDate + " " + saveCurrentTime;
            orderID = neworderID;
        }

        else
        {
            neworderID = saveCurrentDate + " " + saveCurrentTime;
            orderID = orderID;

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
        cartMap.put("orderid", neworderID);
        cartMap.put("image", image);
        cartMap.put("category", category);
        cartMap.put("size", selecteditem);

        //Removing the previous one and making new one
        cartListRef.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                }
            }
        });

        cartListRef.child(User_ID).child(neworderID).updateChildren(cartMap)
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

        if(flag == 0)
        {
            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(category);
            productsRef.child(productID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {
                        Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                        productName.setText(merchandises.getBrandName());
                        Picasso.get().load(merchandises.getImage()).into(productImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

            return;
        }



        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(category);
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                    numberButton.setNumber("1");
                    productName.setText(merchandises.getBrandName());
                    image = merchandises.getImage();
                    Picasso.get().load(merchandises.getImage()).into(productImage);

                    if(selecteditem.equals("S"))
                    {
                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(0));
                        numberButton.setRange(1,final_quantity);
                        productPrice.setText(merchandises.getPrice().get(0));
                    }

                    if(selecteditem.equals("M"))
                    {
                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(1));
                        numberButton.setRange(1,final_quantity);
                        productPrice.setText(merchandises.getPrice().get(1));
                    }

                    if(selecteditem.equals("L"))
                    {
                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(2));
                        numberButton.setRange(1,final_quantity);
                        productPrice.setText(merchandises.getPrice().get(2));
                    }

                    if(selecteditem.equals("XL"))
                    {
                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(3));
                        numberButton.setRange(1,final_quantity);
                        productPrice.setText(merchandises.getPrice().get(3));
                    }

                    if(selecteditem.equals("XXL"))
                    {
                        int final_quantity = Integer.parseInt(merchandises.getQuantity().get(4));
                        numberButton.setRange(1,final_quantity);
                        productPrice.setText(merchandises.getPrice().get(4));
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initializeSpinner(final MyCallback myCallback)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(category);
        productsRef.child(productID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    int idx = 0;
                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                    ArrayList<String> quantity = merchandises.getQuantity();
                    for(int i=0;i<quantity.size();i++)
                    {

                        if(quantity.get(i).equals("0") == false)
                        {
                            if(i == 0)
                            {
                                arraySpinner.add("S");
                            }

                            if(i == 1)
                            {
                                arraySpinner.add("M");
                            }

                            if(i == 2)
                            {
                                arraySpinner.add("L");
                            }

                            if(i == 3)
                            {
                                arraySpinner.add("XL");
                            }

                            if(i == 4)
                            {
                                arraySpinner.add("XXL");
                            }


                        }
                    }

                    myCallback.onCallback(arraySpinner);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

}
