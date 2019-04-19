package com.example.merchandiseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Intent;
import com.firebase.ui.auth.data.model.User;
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
import java.util.List;
import java.util.Map;

public class RequestDetailActivity extends AppCompatActivity
{

    private Button makeRequest;
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
    private int flag2;
    private String selectedSpinneritem;
    private ArrayList<String> size_list;

    public interface MyCallback
    {
        void onCallback(ArrayList<String> value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        arraySpinner = new ArrayList<>();
        image_src = new ArrayList<>();
        size_list = new ArrayList<>();
        orderid_list = new ArrayList<>();
        group_list = new ArrayList<>();

        User_ID = Prevalent.currentOnlineUser;

        productID = getIntent().getStringExtra("pid");
        orderID = getIntent().getStringExtra("order_id");
        image_src = getIntent().getStringArrayListExtra("image");
        category = getIntent().getStringExtra("category");
        group_name = getIntent().getStringExtra("groupName");
        size_list = getIntent().getStringArrayListExtra("size_list");

        makeRequest = (Button) findViewById(R.id.make_request);
        numberButton = findViewById(R.id.numberBtn);
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        SizeSpinner = findViewById(R.id.size_spinner);
        productPrice = findViewById(R.id.productPrice);

        ViewPager viewPager = findViewById(R.id.ViewPager_Inside_Image_2);
        ImageAdapter adapter = new ImageAdapter(this, image_src, "0");
        viewPager.setAdapter(adapter);

        viewPager.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RequestDetailActivity.this, FullPageImageActivity.class);
                intent.putExtra("image", image_src);
                startActivity(intent);
            }
        });

        intializeSpinner(new MyCallback()
        {
            @Override
            public void onCallback(ArrayList<String> value)
            {

                ArrayAdapter<String> myAdapter = new ArrayAdapter<>(RequestDetailActivity.this, android.R.layout.simple_list_item_1, arraySpinner);
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SizeSpinner.setAdapter(myAdapter);

                SizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        selecteditem = SizeSpinner.getSelectedItemPosition();
                        selectedSpinneritem = ((String) SizeSpinner.getSelectedItem());
                        intializefields();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });
            }

        });


        makeRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makeRequest();
                makeRequest2();
            }
        });


    }

    public void onBackPressed()
    {
        Intent intent = new Intent(RequestDetailActivity.this, HomeActivity.class);
        Prevalent_Intent.setIntent(intent);
        intent.putExtra("orderType", Prevalent.currentOrderType);
        startActivity(intent);
    }

    private void makeRequest()
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
        flag2 = 0;

        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Requests").child(productID);
        final DatabaseReference requestRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Requests").child(productID).child("Requests");

        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Category", category);
        requestMap.put("Image", image_src.get(0));
        requestMap.put("IsOpen", "true");
        requestMap.put("PID", productID);
        requestMap.put("Price", productPrice.getText().toString());
        requestMap.put("Size", size_list);


        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    flag2 = 1;
                }
            }
        });


        //UpdateQuantitySum
        final DatabaseReference requestRef3 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Requests").child(productID);
        requestRef3.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild("Quantity"))
                {
                    final DatabaseReference requestRef3 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Requests").child(productID);
                    requestRef3.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            DataSnapshot temp = dataSnapshot.child("Quantity");

                            ArrayList<String> quantity_list = new ArrayList<>();
                            for(DataSnapshot dataSnapshot1 : temp.getChildren())
                            {
                                quantity_list.add(dataSnapshot1.getValue().toString());
                            }

                            for(int i=0;i<size_list.size();i++)
                            {
                                if(i == selecteditem)
                                {
                                    String temp_quantity = quantity_list.get(i);

                                    System.out.println("Chirag1 : " + size_list.size());
                                    System.out.println("Chirag2 : " + temp_quantity);

                                    int result = Integer.parseInt(temp_quantity);
                                    System.out.println("Chirag3 : " + result);
                                    int add_value = Integer.parseInt(numberButton.getNumber());
                                    System.out.println("Chirag4 : " + add_value);

                                    result += add_value;
                                    System.out.println("Chirag5 : " + result);
                                    temp_quantity = Integer.toString(result);
                                    System.out.println("Chirag6 : " + temp_quantity);
                                    quantity_list.set(i,temp_quantity);
                                }
                            }

                            final HashMap<String, Object> requestMap3 = new HashMap<>();
                            requestMap3.put("Quantity", quantity_list);

                            requestRef3.updateChildren(requestMap3).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {

                        }
                    });

                }


                else //If there is no Such Child as Quantity
                {
                    final DatabaseReference requestRef4 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Requests").child(productID);
                    ArrayList<String> quantity_list = new ArrayList<>();
                    for(int i=0;i<size_list.size();i++)
                    {
                        if(i == selecteditem)
                            quantity_list.add(numberButton.getNumber());

                        else
                            quantity_list.add("0");
                    }


                    final HashMap<String, Object> requestMap4 = new HashMap<>();
                    requestMap4.put("Quantity", quantity_list);

                    requestRef4.updateChildren(requestMap4).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {

                            }
                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });




        final HashMap<String, Object> requestMap2 = new HashMap<>();
        requestMap2.put("Address", "");
        requestMap2.put("Contact", "");
        requestMap2.put("Date", saveCurrentDate);
        requestMap2.put("Email", Prevalent.currentEmail);
        requestMap2.put("GroupName", group_name);
        requestMap2.put("IsPaid", "false");
        requestMap2.put("IsPlaced", "false");
        requestMap2.put("OrderID", neworderID);
        requestMap2.put("Quantity", numberButton.getNumber());
        requestMap2.put("Size", selectedSpinneritem);
        requestMap2.put("Status", "incart");
        requestMap2.put("Time", saveCurrentTime);
        requestMap2.put("UserID", User_ID);
        requestMap2.put("UserName", "Chirag");

        requestRef2.child(neworderID).updateChildren(requestMap2).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    /*Toast.makeText(RequestDetailActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RequestDetailActivity.this, TakeRequestDetailsActivity.class);
                    intent.putExtra("orderID", neworderID);
                    intent.putExtra("group_name", group_name);
                    intent.putExtra("product_id", productID);
                    startActivity(intent);*/

                }
            }
        });

    }

    private void makeRequest2()
    {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final String neworderID = saveCurrentDate + " " + saveCurrentTime;
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp").child(User_ID).child(neworderID);

        final HashMap<String, Object> requestMap2 = new HashMap<>();
        requestMap2.put("Address", "");
        requestMap2.put("Contact", "");
        requestMap2.put("Date", saveCurrentDate);
        requestMap2.put("Email", Prevalent.currentEmail);
        requestMap2.put("GroupName", group_name);
        requestMap2.put("IsPaid", "false");
        requestMap2.put("IsPlaced", "false");
        requestMap2.put("OrderID", neworderID);
        requestMap2.put("Quantity", numberButton.getNumber());
        requestMap2.put("Size", selectedSpinneritem);
        requestMap2.put("Status", "incart");
        requestMap2.put("Time", saveCurrentTime);
        requestMap2.put("UserID", User_ID);
        requestMap2.put("ProductID", productID);
        if(image_src.get(0) != null)
            requestMap2.put("Image", image_src.get(0));
        else
            requestMap2.put("Image", "");
        requestMap2.put("UserName", "Chirag");

        requestRef.updateChildren(requestMap2).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(RequestDetailActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RequestDetailActivity.this, TakeRequestDetailsActivity.class);
                    intent.putExtra("orderID", neworderID);
                    intent.putExtra("group_name", group_name);
                    intent.putExtra("product_id", productID);
                    startActivity(intent);

                }
            }
        });
    }

    private void intializefields()
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Merchandise").child(category);
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                    //numberButton.setNumber("1");
                    productName.setText(merchandises.getGroupName());
                    productPrice.setText(merchandises.getPrice());

                    if(merchandises.getImage() != null)
                    {
                        image = merchandises.getImage().get(0);
                        Picasso.get().load(merchandises.getImage().get(0)).into(productImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void intializeSpinner(final MyCallback myCallback)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Merchandise").child(category);
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Merchandise merchandises = dataSnapshot.getValue(Merchandise.class);
                    ArrayList<String> sizes = new ArrayList<>();
                    sizes = merchandises.getSize();

                    for(int i=0;i<sizes.size();i++)
                    {
                        arraySpinner.add(sizes.get(i));
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

