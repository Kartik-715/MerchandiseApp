package com.example.merchandiseapp;

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
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mHomeActivity);
        recyclerView.setLayoutManager(layoutManager);

        String uid = Prevalent.currentOnlineUser ;

        DatabaseReference userGroups = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Groups") ;
        userGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> accessibleGroups = (List<String>) dataSnapshot.getValue() ;
                System.out.println(accessibleGroups) ;



                final String orderType = "1" ; // Selecting Order Type //

                DatabaseReference merch = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(bundle.getString("category","none"));

                merch.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        List<Merchandise> list = new ArrayList<>() ;
                        for(DataSnapshot postdatasnapshot: dataSnapshot.getChildren())
                        {
                            Merchandise mr = postdatasnapshot.getValue(Merchandise.class) ;
                            Set<String> s = new HashSet<>(accessibleGroups) ;
                            if(mr.getAccessGroup() != null)
                            {
                                Set<String> u = new HashSet<>(mr.getAccessGroup()) ;
                                s.retainAll(u) ;
                                if(s.size() != 0 && mr.getOrderType().equals(orderType))
                                {
                                    list.add(mr) ;
                                }
                            }

                        }

                        myAdaptor adaptor = new myAdaptor(mHomeActivity,list) ;
                        recyclerView.setAdapter(adaptor);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Log.d("Error", "Couldn't read this Merchandise") ;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;





    }




}
