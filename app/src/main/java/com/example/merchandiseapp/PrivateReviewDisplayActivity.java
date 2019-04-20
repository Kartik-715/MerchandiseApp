package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Groups;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrivateReviewDisplayActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String productID;
    private String Category;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_review_display);

        Category = "Footwear";
        productID = "F03";
        recyclerView = findViewById(R.id.recyclerReview_private);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String GroupName = Prevalent_Groups.currentGroupName;
        GroupName = "CSEA";

        final DatabaseReference displayRef = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupName).child("Merchandise").child(Category).child(productID).child("Rating");
        final Query queries = displayRef.orderByChild("Category").equalTo(Category);

        displayRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    System.out.println("kartik" + dataSnapshot);
                    DataExists(queries);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void DataExists(Query queries)
    {
        FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                .setQuery(queries, Rating.class)
                .build();

        FirebaseRecyclerAdapter<Rating, reviewsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Rating, reviewsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull reviewsViewHolder holder, int position, @NonNull final Rating model)
            {
                holder.lreview.setText("Review : " + model.getComment());
                holder.lstars.setText("Stars : " + model.getStars());
                holder.luser.setText("UserID : " + model.getUID());
                if(model.getIsPrivate().equals("No"))
                    holder.lstatus.setText("Status : Public" );
                else
                    holder.lstatus.setText("Status : Private");
            }

            @NonNull
            @Override
            public reviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
                reviewsViewHolder holder = new reviewsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
