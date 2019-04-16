package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReviewDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> uid_list;
    private String categoryTxt;
    private String pidTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display);
        pidTxt = getIntent().getStringExtra("pid");
        categoryTxt = getIntent().getStringExtra("category");
        Toast.makeText(ReviewDisplayActivity.this,"hey "+categoryTxt+" "+pidTxt,Toast.LENGTH_LONG).show();
        uid_list = new ArrayList<String>();

        recyclerView = findViewById(R.id.recyclerReview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        final DatabaseReference reviewListRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(categoryTxt).child(pidTxt).child("Rating");

        FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                .setQuery(reviewListRef,Rating.class)
                .build();

        FirebaseRecyclerAdapter<Rating, reviewsViewHolder> adapter
                = new FirebaseRecyclerAdapter<Rating, reviewsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull reviewsViewHolder holder, int position, @NonNull final Rating model)
            {
                holder.lreview.setText( model.getComment());
                holder.lstars.setText( model.getStars().toString() );
                holder.luser.setText(model.getUid());
                uid_list.add(model.getUid());

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


