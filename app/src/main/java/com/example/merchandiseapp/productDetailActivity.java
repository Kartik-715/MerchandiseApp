package com.example.merchandiseapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.example.merchandiseapp.Prevalent.Prevalent_Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class productDetailActivity extends AppCompatActivity
{

    private FloatingActionButton addToCart;

    private Button addToCartButton, buyNowButton, shareButton;
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
    private String selectedSpinneritem;
    private Button btnReviews;
    private Button btnPrivateReviews;
    private RecyclerView recycle;
    private ArrayList<String> size_list;
    private ArrayList<String> quantity_list;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> uid_list;
    private String categoryTxt;
    private String pidTxt;
    private String select;
    private String some;
    private String some2;
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
        recycle = findViewById(R.id.recyclerReview);
        size_list = new ArrayList<>();
        quantity_list = new ArrayList<>();

        productID = getIntent().getStringExtra("pid");
        orderID = getIntent().getStringExtra("order_id");
        image_src = getIntent().getStringArrayListExtra("image");
        category = getIntent().getStringExtra("category");
        group_name = getIntent().getStringExtra("groupName");
        size_list = getIntent().getStringArrayListExtra("size_list");
        quantity_list = getIntent().getStringArrayListExtra("quantity_list2");

        User_ID = Prevalent.currentOnlineUser;
        orderid_list = new ArrayList<>();
        group_list = new ArrayList<>();
        btnPrivateReviews=findViewById(R.id.privateReviews);
        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        buyNowButton = findViewById(R.id.buy_now_Button);
        numberButton = findViewById(R.id.numberBtn);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        SizeSpinner = findViewById(R.id.size_spinner);
        productPrice = findViewById(R.id.productPrice);
        shareButton = findViewById(R.id.share_button);
        btnReviews = findViewById(R.id.reviewbtn);

        shareButton.setVisibility(View.INVISIBLE);

        ViewPager viewPager = findViewById(R.id.ViewPager_Inside_Image);
        ImageAdapter adapter = new ImageAdapter(this, image_src, "0");
        viewPager.setAdapter(adapter);
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(productDetailActivity.this,ReviewDisplayActivity.class);
                //Intent intent = new Intent(HomeActivity.this,ReviewDisplayActivity.class);
                intent.putExtra("category",category);
                intent.putExtra("pid",productID);
                intent.putExtra("select","No");
                startActivity(intent);
                    //select="No";

            }
        });
        btnPrivateReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(productDetailActivity.this,ReviewDisplayActivity.class);
                //Intent intent = new Intent(HomeActivity.this,ReviewDisplayActivity.class);
                intent.putExtra("category",category);
                intent.putExtra("pid",productID);
                intent.putExtra("select","Yes");
                startActivity(intent);
                //select="Yes";
            }
        });
        viewPager.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(productDetailActivity.this, FullPageImageActivity.class);
                intent.putExtra("image", image_src);
                startActivity(intent);
            }
        });

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
                            selectedSpinneritem = ((String) SizeSpinner.getSelectedItem());
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

        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                share();
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

        final HashMap<String,Object> productIDMap = new HashMap<>();
        productIDMap.put("Category", category);
        productIDMap.put("Image", image_src.get(0));
        productIDMap.put("PID", productID);
        productIDMap.put("Price", productPrice.getText().toString());
        productIDMap.put("Size", size_list);
        productIDMap.put("Quantity", quantity_list);
        //productIDMap.put("UserName", Prevalent.currentName);

        cartListRef.child(productID).updateChildren(productIDMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

            }
        });


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("ProductID",productID);
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
        cartMap.put("Size", selectedSpinneritem);
        cartMap.put("UserName", Prevalent.currentName);


        //Removing the previous one and making new one
        final int oneTypeProductPrice = ( Integer.valueOf(productPrice.getText().toString()) ) * ( Integer.valueOf(numberButton.getNumber()) ) ;

        cartListRef.child(productID).child("Orders").child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                }
            }
        });

        /*cartListRef.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                }
            }
        });*/

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

        cartListRef.child(productID).child("Orders").child(neworderID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>()
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
                                Prevalent.currentMoney = Integer.toString(oneTypeProductPrice);
                                orderid_list.add(orderID);
                                group_list.add(group_name);
                                ArrayList<String> product_list = new ArrayList<>();
                                product_list.add(productID);
                                Intent intent = new Intent(productDetailActivity.this, DetailsActivity.class);
                                intent.putExtra("orderid_list", orderid_list);
                                intent.putExtra("group_list", group_list);
                                intent.putExtra("product_list", product_list);
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

        final HashMap<String,Object> productIDMap = new HashMap<>();
        productIDMap.put("Category", category);
        productIDMap.put("Image", image_src.get(0));
        productIDMap.put("PID", productID);
        productIDMap.put("Price", productPrice.getText().toString());
        productIDMap.put("Size", size_list);
        productIDMap.put("Quantity", quantity_list);

        cartListRef.child(productID).updateChildren(productIDMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

            }
        });


        //Updating the Orders Child inside the Product ID
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
        cartMap.put("Size", selectedSpinneritem);
        cartMap.put("UserName", Prevalent.currentName);


        //Removing the previous one and making new one
        /*cartListRef.child(User_ID).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                }
            }
        });*/

        cartListRef.child(productID).child("Orders").child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
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

        cartListRef.child(productID).child("Orders").child(neworderID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        if(task.isSuccessful())
                        {
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
                                                Prevalent_Intent.setIntent(intent);
                                                intent.putExtra("orderType", Prevalent.currentOrderType);
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

    private void share()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, image_src.get(0));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    public void onBackPressed()
    {
        Intent intent = new Intent(productDetailActivity.this, HomeActivity.class);
        intent.putExtra("orderType", Prevalent.currentOrderType);
        Prevalent_Intent.setIntent(intent);
        startActivity(intent);
    }

}
