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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Holder.DeliveredViewHolder;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeliveredActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> orderid_list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered);
        orderid_list = new ArrayList<>();

        recyclerView = findViewById(R.id.delivered_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        final DatabaseReference deliveredList = FirebaseDatabase.getInstance().getReference().child("Orders_Temp");

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(deliveredList.child(Prevalent.currentOnlineUser).orderByChild("Status").equalTo("Delivered"), Order.class)
                .build();

        FirebaseRecyclerAdapter<Order, DeliveredViewHolder> adapter
                = new FirebaseRecyclerAdapter<Order, DeliveredViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull DeliveredViewHolder holder, int position, @NonNull final Order model)
            {
                holder.txtProductDeliveryDate.setText("Delivered on : ");
                holder.txtProductName.setText(model.getGroupName());
                if(model.getImage() != null)
                    Picasso.get().load(model.getImage().get(0)).into(holder.DeliveredImage);
                orderid_list.add(model.getOrderID());
            }

            @NonNull
            @Override
            public DeliveredViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivered_product_layout, viewGroup, false);
                DeliveredViewHolder holder = new DeliveredViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
