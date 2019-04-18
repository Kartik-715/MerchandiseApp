package com.example.merchandiseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.merchandiseapp.Holder.RequestViewHolder;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

public class ViewRequestsActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference requestListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        recyclerView = findViewById(R.id.request_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        String User_ID, User_Email;
        //User_Email = user.getEmail();
        //User_ID = user.getUid();
        User_Email = "mayank@iitg.ac.in";

        String temp_email = User_Email;
        int temp = User_Email.hashCode();
        final String hashcode = Integer.toString(temp);

        Prevalent.currentOnlineUser = hashcode;
        Prevalent.currentEmail = temp_email;

        requestListRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp")
                            .child(Prevalent.currentOnlineUser);
        final Query queries = requestListRef.orderByChild("IsPaid").equalTo("false");

        queries.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
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
        FirebaseRecyclerOptions<Requests> options = new FirebaseRecyclerOptions.Builder<Requests>()
                .setQuery(queries, Requests.class)
                .build();


        FirebaseRecyclerAdapter<Requests, RequestViewHolder> adapter = new FirebaseRecyclerAdapter<Requests, RequestViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder holder, int position, @NonNull final Requests model)
            {
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price: " + model.getSize() + "$");
                holder.txtProductName.setText(model.getGroupName());
                if(model.getImage() != null)
                    Picasso.get().load(model.getImage()).into(holder.RequestImage);

                holder.DeleteButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        requestListRef.child(model.getOrderID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    //Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        final DatabaseReference cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(model.getGroupName())
                                                                .child("Requests").child(model.getProductID()).child("Requests").child(model.getOrderID());

                        String quantity_temp = model.getQuantity();

                        cartListRef2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ViewRequestsActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                /*holder.PayNowButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        Intent intent = new Intent(CartActivity.this, productDetailActivity.class);
                        intent.putExtra("pid", model.getProductID());
                        intent.putExtra("order_id", model.getOrderID());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("category", model.getCategory());
                        intent.putExtra("groupName", model.getGroupName());
                        startActivity(intent);
                    }
                });*/

            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_items_layout, viewGroup, false);
                RequestViewHolder holder = new RequestViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}

