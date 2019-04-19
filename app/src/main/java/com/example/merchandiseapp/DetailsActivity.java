package com.example.merchandiseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity
{
    private ArrayList<String> orderid_list;
    private ArrayList<String> group_list;
    private EditText PhoneNumber, Address, Email_ID;
    private Button Payment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        orderid_list = getIntent().getStringArrayListExtra("orderid_list");
        group_list = getIntent().getStringArrayListExtra("group_list");

        PhoneNumber = (EditText) findViewById(R.id.Booking_Phone_Number);
        Address = (EditText) findViewById(R.id.Booking_Address);
        Email_ID = (EditText) findViewById(R.id.Booking_Email);
        Payment = (Button) findViewById(R.id.Btn_Payment);

        Email_ID.setText(Prevalent.currentEmail);
        Address.setText(Prevalent.currentAddress);
        PhoneNumber.setText(Prevalent.currentPhone);

        Prevalent.currentMoney = "0";

        Payment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProceedToPayment();
            }
        });
    }

    private void ProceedToPayment()
    {

        orderid_list = getIntent().getStringArrayListExtra("orderid_list");
        group_list = getIntent().getStringArrayListExtra("group_list");

        if(PhoneNumber.getText().toString().trim().length() == 0)
        {
            Toast.makeText(DetailsActivity.this, "Please Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
            return;
        }


        if(Address.getText().toString().trim().length() == 0)
        {
            Toast.makeText(DetailsActivity.this, "Address Field can't be Empty. Please Enter Delivery Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Email_ID.getText().toString().trim().length() == 0)
        {
            Toast.makeText(DetailsActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        for(int i=0;i<orderid_list.size();i++)
        {
            String orderid = orderid_list.get(i);
            String group_name = group_list.get(i);

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders_Temp").child(Prevalent.currentOnlineUser).child(orderid);
            final DatabaseReference cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Orders").child(Prevalent.currentOnlineUser).child(orderid);

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("Contact", PhoneNumber.getText().toString());
            cartMap.put("Address", Address.getText().toString());
            cartMap.put("Email",Email_ID.getText().toString());

            cartListRef.updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });

            cartListRef2.updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });
            cartListRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String price = dataSnapshot.child("Price").getValue().toString();
                    System.out.println("Price: " + price);
                    int price_amount = Integer.parseInt(price);

                    String global_price = Prevalent.currentMoney;
                    int global_amount = Integer.parseInt(global_price);
                    System.out.println("Global Price : " + price);

                    global_amount += price_amount;
                    String global_price2 = Integer.toString(global_amount);
                    Prevalent.currentMoney = global_price2;

                    System.out.println("Global_Price_Adding : " + Prevalent.currentMoney);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        System.out.println("Kartik : " + Prevalent.currentMoney);
        Intent intent = new Intent(DetailsActivity.this, PaymentActivity.class);
        startActivity(intent);

    }

}
