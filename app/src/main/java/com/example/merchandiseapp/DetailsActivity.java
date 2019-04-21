package com.example.merchandiseapp;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private ArrayList<String> group_list, product_list;
    private EditText PhoneNumber, Address, Email_ID;
    private Button Payment;
    private TextView Txt_Total_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        orderid_list = getIntent().getStringArrayListExtra("orderid_list");
        group_list = getIntent().getStringArrayListExtra("group_list");
        product_list = getIntent().getStringArrayListExtra("product_list");

        PhoneNumber = (EditText) findViewById(R.id.Booking_Phone_Number);
        Address = (EditText) findViewById(R.id.Booking_Address);
        Email_ID = (EditText) findViewById(R.id.Booking_Email);
        Payment = (Button) findViewById(R.id.Btn_Payment);
        Txt_Total_Price = findViewById(R.id.Txt_Total_Price);

        Txt_Total_Price.setText("Total Price : Rs" + Prevalent.currentMoney);
        Email_ID.setText(Prevalent.currentEmail);
        Address.setText(Prevalent.currentAddress);
        PhoneNumber.setText(Prevalent.currentPhone);

        updateWallet();

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
            String product_name = product_list.get(i);

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders_Temp").child(Prevalent.currentOnlineUser).child(orderid);
            final DatabaseReference cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(group_name).child("Orders").child(product_name).child("Orders").child(orderid);

            cartListRef.child("Contact").setValue(PhoneNumber.getText().toString());
            cartListRef.child("Address").setValue(Address.getText().toString());
            cartListRef.child("Email").setValue(Email_ID.getText().toString());

            cartListRef2.child("Contact").setValue(PhoneNumber.getText().toString());
            cartListRef2.child("Address").setValue(Address.getText().toString());
            cartListRef2.child("Email").setValue(Email_ID.getText().toString());

            /*final HashMap<String, Object> cartMap = new HashMap<>();
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });*/
        }

        //  System.out.println("Kartik : " + Prevalent.currentMoney);
        Intent intent = new Intent(DetailsActivity.this, PaymentActivity.class);
        intent.putExtra("orderid_list", orderid_list);
        intent.putExtra("group_list", group_list);
        intent.putExtra("product_list", product_list);
        startActivity(intent);

    }

    public void onBackPressed()
    {
        Intent intent = new Intent(DetailsActivity.this, CartActivity.class);
        startActivity(intent);
    }

    public void updateWallet()
    {
        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
        UserData.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Wallet_Money").exists())
                {
                    Prevalent.currentWalletMoney = dataSnapshot.child("Wallet_Money").getValue().toString();
                }

                else
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Wallet_Money", "0");
                    Prevalent.currentWalletMoney = "0";

                    UserData.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}
