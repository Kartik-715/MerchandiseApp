package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentItem extends Fragment
{
    final ArrayList<Merchandise> array_merchandise = new ArrayList<>();
    private HomeActivity mHomeActivity ;
    Bundle bundle ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeActivity = (HomeActivity) getActivity() ;
        bundle = this.getArguments() ;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false) ;
        setupRecyclerView(rv) ;
        return rv ;
    }

    public FragmentItem()
    {

    }

    private void setupRecyclerView(final RecyclerView recyclerView)
    {
        ArrayList<String> array_test = new ArrayList<>();

        array_test.add("CSEA");
        array_test.add("CSEA");
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mHomeActivity);
        recyclerView.setLayoutManager(layoutManager);

        final List<String> accessibleGroups = new ArrayList<>();
        accessibleGroups.add("CSEA") ;

        DatabaseReference allGroups = FirebaseDatabase.getInstance().getReference().child("Group");
        System.out.println("Test" + allGroups);

        allGroups.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                HashMap<String, Object> All_Groups = (HashMap<String, Object>) dataSnapshot.getValue();
                List<Merchandise> list = new ArrayList<>() ;


                for(String o: accessibleGroups)
                {
                    HashMap<String,Object> group = (HashMap<String, Object>) All_Groups.get(o) ;
                    HashMap<String,Object> groupMerch = (HashMap<String, Object>) group.get("Merchandise") ;
                    HashMap<String,Object> reqdMerch = (HashMap<String, Object>) groupMerch.get(bundle.getString("category", "none")) ;

                    //System.out.println("Group Details: " + group );
                    //System.out.println("Group Merchandise: " + groupMerch );

                    for(Map.Entry<String, Object> merch : reqdMerch.entrySet())
                    {
                        Merchandise mr =  (Merchandise) merch.getValue();
                        System.out.println("Merch : " + merch);
                        list.add(mr) ;
                    }

                }

                myAdaptor adaptor = new myAdaptor(mHomeActivity,list) ;
                recyclerView.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


    }




}
