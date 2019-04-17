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
    private String group_name = "";
    private ArrayList<String> orderid_list;
    private ArrayList<String> group_list;
    private ArrayList<String> image_src;
    private Spinner SizeSpinner;
    private int selecteditem;
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

        image_src = new ArrayList<>();

        productID = getIntent().getStringExtra("pid");
        orderID = getIntent().getStringExtra("order_id");
        image_src = getIntent().getStringArrayListExtra("image");
        category = getIntent().getStringExtra("category");
        group_name = getIntent().getStringExtra("groupName");

        User_ID = Prevalent.currentOnlineUser;
        orderid_list = new ArrayList<>();
        group_list = new ArrayList<>();

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
                            selecteditem = SizeSpinner.getSelectedItemPosition();
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


        final String neworderID;

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

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Orders");
        final DatabaseReference cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Orders_Temp");


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("ProductID",productID);
        //cartMap.put("pname",productName.getText().toString());
        cartMap.put("GroupName",productName.getText().toString());
        cartMap.put("Price",productPrice.getText().toString());
        cartMap.put("Date",saveCurrentDate);
        cartMap.put("Time",saveCurrentTime);
        cartMap.put("Contact", "");
        cartMap.put("Address", "");
        cartMap.put("Email","");
        cartMap.put("IsPlaced","false");
        cartMap.put("Status","incart");
        cartMap.put("Quantity",numberButton.getNumber());
        cartMap.put("UserID", User_ID);
        cartMap.put("OrderID", neworderID);
        cartMap.put("Image", image_src);
        cartMap.put("Category", category);
        cartMap.put("Size", selecteditem);

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

        cartListRef2.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
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
                    /*orderid_list.add(orderID);
                    Intent intent = new Intent(productDetailActivity.this, DetailsActivity.class);
                    intent.putExtra("orderid_list", orderid_list);
                    startActivity(intent);
*/
                    cartListRef2.child(User_ID).child(neworderID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                            if(task.isSuccessful())
                            {
                                orderid_list.add(orderID);
                                group_list.add(group_name);
                                Intent intent = new Intent(productDetailActivity.this, DetailsActivity.class);
                                intent.putExtra("orderid_list", orderid_list);
                                intent.putExtra("group_list", group_list);
                                startActivity(intent);
                            }
                        }
                    });
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
        final String neworderID;

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
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Orders");
        final DatabaseReference cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Orders_Temp");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("ProductID",productID);
        //cartMap.put("pname",productName.getText().toString());
        cartMap.put("GroupName",productName.getText().toString());
        cartMap.put("Price",productPrice.getText().toString());
        cartMap.put("Date",saveCurrentDate);
        cartMap.put("Time",saveCurrentTime);
        cartMap.put("Contact", "");
        cartMap.put("Address", "");
        cartMap.put("Email","");
        cartMap.put("IsPlaced","false");
        cartMap.put("Status","incart");
        cartMap.put("Quantity",numberButton.getNumber());
        cartMap.put("UserID", User_ID);
        cartMap.put("OrderID", neworderID);
        cartMap.put("Image", image_src);
        cartMap.put("Category", category);
        cartMap.put("Size", selecteditem);

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

        cartListRef2.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
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
                           /* Toast.makeText(productDetailActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(productDetailActivity.this, HomeActivity.class);
                            startActivity(intent);*/

                            cartListRef2.child(User_ID).child(neworderID).updateChildren(cartMap)
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
                });
    }

    private void getProductDetails(String productID)
    {

        if(flag == 0)
        {

            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Merchandise").child(category);
            productsRef.child(productID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {
                        Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                        productName.setText(merchandises.getGroupName());
                        if(merchandises.getImage() != null)
                            Picasso.get().load(merchandises.getImage().get(0)).into(productImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

            return;
        }


        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Merchandise").child(category);
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                    numberButton.setNumber("1");
                    productName.setText(merchandises.getGroupName());
                    productPrice.setText(merchandises.getPrice());

                    if(merchandises.getImage() != null)
                    {
                        image = merchandises.getImage().get(0);
                        Picasso.get().load(merchandises.getImage().get(0)).into(productImage);
                    }

                    int final_quantity = Integer.parseInt(merchandises.getQuantity().get(selecteditem));
                    numberButton.setRange(1,final_quantity);

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

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Merchandise").child(category).child(productID);
        productsRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    int idx = 0;
                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                    ArrayList<String> sizes = merchandises.getSize();
                    for(int i=0;i<sizes.size();i++)
                        arraySpinner.add(sizes.get(i));

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
