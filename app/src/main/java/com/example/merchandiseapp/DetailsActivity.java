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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity
{
    private ArrayList<String> orderid_list;
    private EditText PhoneNumber, Address, Email_ID;
    private Button Payment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        orderid_list = getIntent().getStringArrayListExtra("orderid_list");
        PhoneNumber = (EditText) findViewById(R.id.Booking_Phone_Number);
        Address = (EditText) findViewById(R.id.Booking_Address);
        Email_ID = (EditText) findViewById(R.id.Booking_Email);
        Payment = (Button) findViewById(R.id.Btn_Payment);

        Email_ID.setText(Prevalent.currentEmail);

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
        for(int i=0;i<orderid_list.size();i++)
        {
            String orderid = orderid_list.get(i);

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser).child(orderid);
            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("contact", PhoneNumber.getText().toString());
            cartMap.put("address", Address.getText().toString());
            cartMap.put("email",Email_ID.getText().toString());

            cartListRef.updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });
        }




    }

}
