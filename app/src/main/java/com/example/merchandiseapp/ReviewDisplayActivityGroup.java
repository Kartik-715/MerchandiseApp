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

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewDisplayActivityGroup extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> uid_list;
    private String categoryTxt;
    private String pidTxt;
    private String select;
    private String some;
    private String some2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_display);


        pidTxt = getIntent().getStringExtra("PID");
        categoryTxt = getIntent().getStringExtra("Category");
        uid_list = new ArrayList<String>();
        select = getIntent().getStringExtra("select");
        recyclerView = findViewById(R.id.recyclerReview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final DatabaseReference fun = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(categoryTxt).child(pidTxt);
        fun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                some = (String) dataSnapshot.child("GroupName").getValue();
                some2=(String) dataSnapshot.child("GroupName").getValue();
                if (select.equals("Yes")) {
                    select = (String) dataSnapshot.child("GroupName").getValue();

                    //Toast.makeText(ReviewDisplayActivityGroup.this,select,Toast.LENGTH_LONG).show();
                }
                display();
                //Toast.makeText(ReviewDisplayActivityGroup.this,"hey "+some +" "+some2+" "+categoryTxt + " " + pidTxt+" " +select,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       // Toast.makeText(ReviewDisplayActivityGroup.this,"hey "+some +" "+some2+" "+categoryTxt + " " + pidTxt+" "+select,Toast.LENGTH_LONG).show();
//        if (select == "Yes") {
//            select = some;
//        }

    }


    //@Override
    protected void display() {
        //Toast.makeText(ReviewDisplayActivityGroup.this," "+some +" "+some2+" "+categoryTxt + " " + pidTxt+" " +select,Toast.LENGTH_LONG).show();
        super.onStart();
        //Toast.makeText(ReviewDisplayActivityGroup.this,"hey "+categoryTxt+"********************** "+pidTxt,Toast.LENGTH_LONG).show();
        final DatabaseReference reviewListRef = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(categoryTxt).child(pidTxt).child("Rating");

        if (select.equals("No")) {
            FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                    .setQuery(reviewListRef.orderByChild("IsPrivate").equalTo("No"), Rating.class)
                    .build();
            FirebaseRecyclerAdapter<Rating, reviewsViewHolder> adapter
                    = new FirebaseRecyclerAdapter<Rating, reviewsViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull reviewsViewHolder holder, int position, @NonNull final Rating model) {

                    holder.lreview.setText(model.getComment());
                    holder.lstars.setText(model.getStars());
                    holder.luser.setText(model.getUID());
                    uid_list.add(model.getUID());
                    //Toast.makeText(ReviewDisplayActivityGroup.this, "hey " + model.getComment() + " " + model.getStars() + " " + model.getUID(), Toast.LENGTH_LONG).show();
                }

                @NonNull
                @Override
                public reviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
                    reviewsViewHolder holder = new reviewsViewHolder(view);
                    return holder;
                }
            };

            recyclerView.setAdapter(adapter);
            adapter.startListening();


        } else {
            //Toast.makeText(ReviewDisplayActivityGroup.this,"hey "+some +" "+some2+" "+categoryTxt + " " + pidTxt+" " +select,Toast.LENGTH_LONG).show();
            FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                    .setQuery(reviewListRef.orderByChild("Group").equalTo(select), Rating.class)
                    .build();
            FirebaseRecyclerAdapter<Rating, reviewsViewHolder> adapter
                    = new FirebaseRecyclerAdapter<Rating, reviewsViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull reviewsViewHolder holder, int position, @NonNull final Rating model) {

                    holder.lreview.setText(model.getComment());
                    holder.lstars.setText(model.getStars());
                    holder.luser.setText(model.getUID());
                    uid_list.add(model.getUID());
                    //Toast.makeText(ReviewDisplayActivityGroup.this, "hey " + model.getComment() + " " + model.getStars() + " " + model.getUID(), Toast.LENGTH_LONG).show();
                }

                @NonNull
                @Override
                public reviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
                    reviewsViewHolder holder = new reviewsViewHolder(view);
                    return holder;
                }
            };

            recyclerView.setAdapter(adapter);
            adapter.startListening();


        }

//        FirebaseRecyclerAdapter<Rating, reviewsViewHolder> adapter
//                = new FirebaseRecyclerAdapter<Rating, reviewsViewHolder>(options)
//        {
//            @Override
//            protected void onBindViewHolder(@NonNull reviewsViewHolder holder, int position, @NonNull final Rating model)
//            {
//
//                holder.lreview.setText( model.getComment());
//                holder.lstars.setText( model.getStars() );
//                holder.luser.setText(model.getUID());
//                uid_list.add(model.getUID());
//                Toast.makeText(ReviewDisplayActivityGroup.this,"hey "+model.getComment()+" "+model.getStars()+" "+model.getUID(),Toast.LENGTH_LONG).show();
//            }
//
//            @NonNull
//            @Override
//            public reviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
//            {
//                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
//                reviewsViewHolder holder = new reviewsViewHolder(view);
//                return holder;
//            }
//        };
//
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }

    }
}
