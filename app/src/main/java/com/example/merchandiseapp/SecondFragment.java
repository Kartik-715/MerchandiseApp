package com.example.merchandiseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SecondFragment extends Fragment {
    // Store instance variables

    private DatabaseReference ProductsRef;
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public ImageView imageView;
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    String Category;
    String PID;
    String GroupName;


    private TextView a;

    private   ArrayList<String> Contact;
    private   ArrayList<String> Email ;
    private   ArrayList<String> Quantity ;
    private   ArrayList<String> Size ;
    private   ArrayList<String> UserName ;
    private   ArrayList<String> UserID ;

    // Store instance variables
    private String title;
    private int page;
    static Bundle args = new Bundle();

    // newInstance constructor for creating fragment with arguments
    public static SecondFragment newInstance(int page, String title , String Category , String PID,String GroupName) {

        SecondFragment fragmentSecond = new SecondFragment();
        args.putInt("someInt", page);
        args.putString("someTitle", "Paid");
        args.putString("productID" , PID);
        args.putString("category" , Category);
        args.putString("group_name" ,GroupName);
        return fragmentSecond;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {


//        System.out.println(getArguments().getString("category"));

        super.onCreate(savedInstanceState);

        System.out.println("in 2");
        page = args.getInt("someInt", 0);
        title = args.getString("someTitle");

        Category = args.getString("category");
        GroupName = args.getString("group_name");
        PID = args.getString("productID");






//        if(get)
//        final  Fragment fragment =new FirstFragment();
//        final Bundle bundle = new Bundle();













    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
//        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Group").child(GroupName).child("Requests").child(PID).child("Requests");

        list = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long ctr=0;
                listView = view.findViewById(R.id.viewNonPaid);
                Contact = new ArrayList<String>();
                Email = new ArrayList<String>();
                Quantity = new ArrayList<String>();
                Size = new ArrayList<String>();
                UserName = new ArrayList<String>();
                UserID = new ArrayList<String>();

                long count = dataSnapshot.getChildrenCount();
                for (DataSnapshot ds : dataSnapshot.getChildren()){

//                    System.out.println(ds.child("IsPaid").getValue().toString());
                    System.out.println(ds.child("IsPaid").getValue().toString());
                    HashMap<String, Object> PaidMembers = (HashMap<String , Object>) dataSnapshot.getValue();

                    if (ds.child("IsPaid").getValue() != null && ds.child("IsPaid").getValue().toString().equals("false")) {

                        Contact.add((String)ds.child("Contact").getValue());
                        UserID.add((String)ds.child("UserID").getValue());
                        Email.add((String)ds.child("Email").getValue());
                        Quantity.add((String)ds.child("Quantity").getValue());
                        Size.add((String)ds.child("Size").getValue());
                        UserName.add((String)ds.child("UserName").getValue());

//                                    System.out.println( ds.child("UserID").toString());
////                                    System.out.println( ds.child("Contact").toString());
//                                    System.out.println( ds.child("Email").toString());
//                                    System.out.println( ds.child("Quantity").toString());
//                                    System.out.println( ds.child("Size").toString());
                    }
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
                        CustomAdapter customAdapter = new CustomAdapter(getActivity().getApplicationContext(),

                                Contact,
                                Email,
                                Quantity,
                                Size,
                                UserName

                        );
                        listView.setAdapter(customAdapter);


                    }
                }


/*
*
*
*
* HashMap<String, Object> All_merchandise = (HashMap<String, Object>) dataSnapshot.getValue();
                System.out.println(All_merchandise);


                for (Object o : All_merchandise.entrySet()) {
                    HashMap.Entry p1 = (HashMap.Entry) o;
                    FragmentItem fragment = new FragmentItem();
                    Bundle bundle = new Bundle();
                    bundle.putString("category", (String) p1.getKey());
                    fragment.setArguments(bundle);
                    adaptor.AddFragment(fragment, (String) p1.getKey());
*
* */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}