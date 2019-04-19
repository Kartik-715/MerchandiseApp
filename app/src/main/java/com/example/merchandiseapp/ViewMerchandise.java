package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewMerchandise extends AppCompatActivity {

    private String GroupName;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_merchandise);


        GroupName = "CSEA" ; /***** TO DO PREVALENT CURRENT GROUP NAME ******/
        tabLayout = findViewById(R.id.tabLayout2);
        viewPager = findViewById(R.id.viewPager_id2);
        final ViewPagerAdaptor adaptor = new ViewPagerAdaptor(getSupportFragmentManager());

        DatabaseReference merch = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupName).child("Merchandise");
        merch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //adaptor.clearFragments();

                for (DataSnapshot postdatasnapshot : dataSnapshot.getChildren()) {
                    List<Merchandise> list = new ArrayList<>();
                    for (DataSnapshot merchandise : postdatasnapshot.getChildren()) {
                        //System.out.println(merchandise) ;
                        Merchandise mr = merchandise.getValue(Merchandise.class);
                        list.add(mr);

                    }

                    if (list.size() != 0) {
                        FragmentItemViewMerch fragment = new FragmentItemViewMerch();
                        Bundle bundle = new Bundle(); // Incase you want to pass some arguments into Fragment //
                        fragment.setObject(list);
                        fragment.setArguments(bundle);
                        System.out.println("Added a fragment!");
                        adaptor.AddFragment(fragment, (String) postdatasnapshot.getKey());

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewPager.setAdapter(adaptor);
        tabLayout.setupWithViewPager(viewPager);


    }


}
