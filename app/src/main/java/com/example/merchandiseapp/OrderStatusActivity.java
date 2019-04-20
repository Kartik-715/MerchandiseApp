package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.merchandiseapp.Holder.DeliveredViewHolder;
import com.example.merchandiseapp.Holder.OrderStatusHolder;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderStatusActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> orderid_list;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        orderid_list = new ArrayList<>();

        recyclerView = findViewById(R.id.orders_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Order Status");
        loadingBar.setMessage("Please wait, while we are Displaying Images for you");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        final DatabaseReference deliveredList = FirebaseDatabase.getInstance().getReference().child("Orders_Temp");

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(deliveredList.child(Prevalent.currentOnlineUser).orderByChild("IsPlaced").equalTo("true"), Order.class)
                .build();

        FirebaseRecyclerAdapter<Order, OrderStatusHolder> adapter
                = new FirebaseRecyclerAdapter<Order, OrderStatusHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull OrderStatusHolder holder, int position, @NonNull final Order model)
            {
                holder.txtProductName.setText("Product Name : " + model.getGroupName());
                holder.txtProductGroup.setText("Placed On : " + model.getDate());
                holder.txtProductStatus.setText("Current Status : " + model.getStatus());
                holder.txtProductPrice.setText("Price : " + model.getPrice() + " -- Quantity : " + model.getQuantity());

                if(model.getImage() != null)
                    Picasso.get().load(model.getImage().get(0)).into(holder.DeliveredImage);

                loadingBar.dismiss();
                orderid_list.add(model.getOrderID());
            }

            @NonNull
            @Override
            public OrderStatusHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_status_layout, viewGroup, false);
                OrderStatusHolder holder = new OrderStatusHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
