package com.example.merchandiseapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.merchandiseapp.Holder.OrderHistoryHolder;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import okio.Options;

public class Order_History extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutmanager;
    private TextView txtproductname;
    private TextView txtproductquantity;
    private TextView txtproductprice;
    private TextView txtorderid;
    private TextView txtproductstatus;
    private TextView txtorderdate;

    @Override
    protected void onCreate (Bundle savedInstancestate)
    {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_order_history);

        recyclerView=findViewById(R.id.orders_list);
        recyclerView.setHasFixedSize(true);
        layoutmanager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);

    }
    @Override
    protected void onStart(){

        super.onStart();


        final DatabaseReference OrdersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser);;
        FirebaseRecyclerOptions<Order> Options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(OrdersRef,Order.class)//edit requird
                .build();

        FirebaseRecyclerAdapter<Order,OrderHistoryHolder>adapter
                =new FirebaseRecyclerAdapter<Order, OrderHistoryHolder>(Options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position, @NonNull Order model) {
                holder.txtProductName.setText("Product: "+model.getPname());
                holder.txtProductQuantity.setText("Quantity: "+model.getQuantity());
                holder.txtProductPrice.setText("Price: "+model.getPrice()+"$");
                holder.txtOrderid.setText("OrderID: "+model.getOrderid());
                holder.txtProductStatus.setText("Status: "+model.getStatus());
                holder.txtOrderDate.setText("Date Ordered: "+model.getDate());
                Picasso.get().load(model.getImage()).into(holder.ProductImage);
            }

            @NonNull
            @Override
            public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);
                OrderHistoryHolder holder = new OrderHistoryHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


}


