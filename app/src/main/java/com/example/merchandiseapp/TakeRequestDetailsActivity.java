package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class TakeRequestDetailsActivity extends AppCompatActivity
{
    private ArrayList<String> orderid_list;
    private ArrayList<String> group_list;
    private EditText PhoneNumber, Address, Email_ID;
    private Button Pay_Now, Pay_Later;
    private String orderID, groupName, productID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_request_details);

        PhoneNumber = (EditText) findViewById(R.id.Request_Phone_Number);
        Address = (EditText) findViewById(R.id.Request_Address);
        Email_ID = (EditText) findViewById(R.id.Request_Email);
        Pay_Now = (Button) findViewById(R.id.Btn_Pay_Now);
        Pay_Later = (Button) findViewById(R.id.Btn_Pay_Later);

        orderID = getIntent().getStringExtra("orderID");
        groupName = getIntent().getStringExtra("group_name");
        productID = getIntent().getStringExtra("product_id");

        Pay_Now.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Pay_Now_Function2();
                Pay_Now_Function();
            }
        });

        Pay_Later.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Pay_Later_Function2();
                Pay_Later_Function();
            }
        });
    }

    private void Pay_Now_Function()
    {
        if(PhoneNumber.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
            return;
        }


        if(Address.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Address Field can't be Empty. Please Enter Delivery Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Email_ID.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Group").child(groupName).child("Requests").child(productID).child("Requests").child(orderID);

        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",Email_ID.getText().toString());
        requestMap.put("IsPaid", "true");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(TakeRequestDetailsActivity.this, "Successfully Paid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Pay_Later_Function()
    {
        if(PhoneNumber.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
            return;
        }


        if(Address.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Address Field can't be Empty. Please Enter Delivery Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Email_ID.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Group").child(groupName).child("Requests").child(productID).child("Requests").child(orderID);

        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",Email_ID.getText().toString());
        requestMap.put("IsPaid", "false");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(TakeRequestDetailsActivity.this, "Successfully Paid", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Pay_Now_Function2()
    {
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp").child(Prevalent.currentOnlineUser).child(orderID);
        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",Email_ID.getText().toString());
        requestMap.put("IsPaid", "true");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

            }
        });
    }

    private void Pay_Later_Function2()
    {
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp").child(Prevalent.currentOnlineUser).child(orderID);
        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",Email_ID.getText().toString());
        requestMap.put("IsPaid", "false");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

            }
        });

    }

}
