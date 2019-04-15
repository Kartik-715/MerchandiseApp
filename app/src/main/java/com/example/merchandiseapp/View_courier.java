package com.example.merchandiseapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class View_courier extends AppCompatActivity {


    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    // Vendor vendor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courier);
        //  vendor = new Vendor();
        listView = (ListView)findViewById(R.id.view_courier);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.info,R.id.Info,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                    if (ds.child("AccessLevel").getValue() != null && ds.child("AccessLevel").getValue().toString().equals("1")) {
                        Context context = getApplicationContext();
                        Toast.makeText(context, "Yay!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                       /* if(ds.child("Name").getValue().toString().equals("Admin")) {
                         continue;
                        }
                        vendor = ds.getValue(Vendor.class);*/
                        if(ds.child("Name").getValue() != null) list.add("Name : " + ds.child("Name").getValue().toString());
                        if(ds.child("EmailID").getValue() != null) list.add("Email : " + ds.child("EmailID").getValue().toString());
                        if(ds.child("Contact").getValue() != null) list.add("Contact : " + ds.child("Contact").getValue().toString());
                        if(ds.child("Address").getValue() != null) list.add("Address : " + ds.child("Address").getValue().toString());
                        if(ds.child("UPI").getValue() != null) list.add("UPI : " + ds.child("UPI").getValue().toString());
                        list.add("\n");

                      /*  list.add(vendor.getUsername().toString() + "\n" + vendor.getEmailID().toString() + "\n"
                                + vendor.getContact().toString() + "\n" + vendor.getAddress().toString() + "\n" +
                                vendor.getUPI().toString());*/
                    }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
