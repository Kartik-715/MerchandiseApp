package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentItem extends Fragment
{
    private Context mHomeActivity;
    Bundle bundle;
    ProgressDialog loadingBar;
    private List<Merchandise> list ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHomeActivity = getActivity();
        bundle = this.getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    public FragmentItem()
    {

    }

    /*public FragmentItem(ProgressDialog loadingBar)
    {
        this.loadingBar = loadingBar;
    }*/



    private void setupRecyclerView(final RecyclerView recyclerView)
    {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mHomeActivity);
        recyclerView.setLayoutManager(layoutManager);
        myAdaptor adaptor = new myAdaptor(mHomeActivity, list);
        recyclerView.setAdapter(adaptor);
    }


    public void setObject(List<Merchandise> x)
    {
        this.list = x ;
    }




}
