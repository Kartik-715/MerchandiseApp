package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class orderRequestDetails extends AppCompatActivity {

    String Category,PID,GroupName;

    private DatabaseReference ProductsRef;
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public ImageView imageView;
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref,myRef;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    private TextView a;

    private   ArrayList<String> Contact;
    private   ArrayList<String> Email ;
    private   ArrayList<String> Quantity ;
    private   ArrayList<String> Size ;
    private   ArrayList<String> UserName ;
    private   ArrayList<String> UserID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request_details);

        Category = getIntent().getStringExtra("Category");
        PID = getIntent().getStringExtra("PID");
        GroupName = getIntent().getStringExtra("GroupName");

        System.out .println("!!@@" + Category+PID+GroupName);

        final DatabaseReference myref;

        myref = FirebaseDatabase.getInstance().getReference().child("Group")
                .child(GroupName).child("Orders").child(PID);

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                long ctr=0 , count;
                count = dataSnapshot.child("Orders").getChildrenCount();
                listView = (ListView) findViewById(R.id.viewOrders);
                Contact = new ArrayList<String>();
                Email = new ArrayList<String>();
                Quantity = new ArrayList<String>();
                Size = new ArrayList<String>();
                UserName = new ArrayList<String>();
                UserID = new ArrayList<String>();

                for (DataSnapshot ds : dataSnapshot.child("Orders").getChildren()) {

//                    System.out.println(ds.child("IsPaid").getValue().toString());

                    Contact.add((String) ds.child("Contact").getValue());
                    UserID.add((String) ds.child("UserID").getValue());
                    Email.add((String) ds.child("Email").getValue());
                    Quantity.add((String) ds.child("Quantity").getValue());
                    Size.add((String) ds.child("Size").getValue());
                    UserName.add((String) ds.child("UserName").getValue());

//                                    System.out.println( ds.child("UserID").toString());
////                                    System.out.println( ds.child("Contact").toString());
//                                    System.out.println( ds.child("Email").toString());
//                                    System.out.println( ds.child("Quantity").toString());
//                                    System.out.println( ds.child("Size").toString());

                    ctr++;

                    if(ctr  == count)
                    {

                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
                        System.out.println(UserID);
                        System.out.println(Contact);
                        System.out.println(Email);
                        System.out.println(Quantity);
                        System.out.println(Size);
                        System.out.println(UserName);

                        ArrayList<String> listNew = new ArrayList<String>();


                        for( int i=0 ; i<Contact.size(); i++)
                        {
                            listNew.add( "User ID : " + UserID.get(i) + "\n" + "Contact : " + Contact.get(i) + "\nEmail - ID : " + Email.get(i) + "\n" + "Quantity : " + Quantity.get(i) +"\n" +
                                    "Size : " + Size.get(i) + "\n" + "User Name : " + UserName.get(i) +"\n\n");
                        }



//                        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1 , listNew);
//                        listView.setAdapter(adapter);
//                        System.out.println("hello"+adapter);
                        CustomAdapter2 customAdapter = new CustomAdapter2(orderRequestDetails.this,

                                Contact,
                                Email,
                                Quantity,
                                Size,
                                UserName

                        );
                        listView.setAdapter(customAdapter);




                    }


                }











            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });























    }
}
