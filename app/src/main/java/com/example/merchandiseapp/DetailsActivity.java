package com.example.merchandiseapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity
{
    private ArrayList<String> orderid_list;
    private EditText PhoneNumber, Address;
    private Button Payment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        orderid_list = getIntent().getStringArrayListExtra("orderid_list");
        PhoneNumber = (EditText) findViewById(R.id.Booking_Phone_Number);
        Address = (EditText) findViewById(R.id.Booking_Address);
        Payment = (Button) findViewById(R.id.Btn_Payment);



    }

}
