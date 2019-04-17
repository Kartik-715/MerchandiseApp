package com.example.merchandiseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FirstFragment extends Fragment {
    private   ArrayList<String> Contact;
    private   ArrayList<String> Email ;
    private   ArrayList<String> Quantity ;
    private   ArrayList<String> Size ;
    private   ArrayList<String> UserName ;
    private   ArrayList<String> UserID ;

    // Store instance variables
    private String title;
    private int page;
   private ArrayList<String> paid;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page, String title) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Contact = new ArrayList<String>();
        Email = new ArrayList<String>();
        Quantity = new ArrayList<String>();
        Size = new ArrayList<String>();
        UserName = new ArrayList<String>();
        UserID = new ArrayList<String>();


        page = getArguments().getInt("someInt", 0);



        title = getArguments().getString("someTitle");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            UserID = getArguments().getStringArrayList("UserID");
            Contact = getArguments().getStringArrayList("Contact");
            Email = getArguments().getStringArrayList("Email");
            Quantity = getArguments().getStringArrayList("Quantity");
            Size = getArguments().getStringArrayList("Size");
            UserName = getArguments().getStringArrayList("UserName");
        }

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);



        //        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(paid !=null) {
            System.out.println(UserID);
            System.out.println(Contact);
            System.out.println(Email);
            System.out.println(Quantity);
            System.out.println(Size);
            System.out.println(UserName);
        }

    }
}