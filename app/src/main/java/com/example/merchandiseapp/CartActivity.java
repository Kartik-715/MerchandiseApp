package com.example.merchandiseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private ArrayList<String> orderid_list;
    private ArrayList<String> group_list;
    private DatabaseReference cartListRef;
    private ImageView ImageEmptyCart;
    private Button BtnShopNow;
    private TextView TxtEmptyCart;
    private int countCards;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        orderid_list = new ArrayList<>();
        group_list = new ArrayList<>();

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = findViewById(R.id.next_process_btn);
        ImageEmptyCart = findViewById(R.id.ivEmptyStates);
        BtnShopNow = findViewById(R.id.shop_now_button);
        TxtEmptyCart = findViewById(R.id.tvInfo);

        NextProcessBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NextActivity();
            }
        });

    }

    private void NextActivity()
    {
        Intent intent = new Intent(CartActivity.this, DetailsActivity.class);
        intent.putExtra("orderid_list", orderid_list);
        intent.putExtra("group_list", group_list);
        startActivity(intent);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Orders_Temp").child(Prevalent.currentOnlineUser);
        final Query queries = cartListRef.orderByChild("IsPlaced").equalTo("false");
        System.out.println(Prevalent.currentOnlineUser);

        queries.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                countCards = (int) dataSnapshot.getChildrenCount();

                if(dataSnapshot.exists())
                {
                    //Toast.makeText(CartActivity.this,"data exists",Toast.LENGTH_SHORT).show();
                    DataExists(queries);
                }
                else
                {
                    //Toast.makeText(CartActivity.this,"no data exists",Toast.LENGTH_SHORT).show();
                    NoDataExists();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public void onBackPressed()
    {
        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
        intent.putExtra("orderType", Prevalent.currentOrderType);
        startActivity(intent);
    }

    private void NoDataExists()
    {
        ImageEmptyCart.setVisibility(View.VISIBLE);
        BtnShopNow.setVisibility(View.VISIBLE);
        TxtEmptyCart.setVisibility(View.VISIBLE);
        NextProcessBtn.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        BtnShopNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                intent.putExtra("orderType", Prevalent.currentOrderType);
                startActivity(intent);
            }
        });
    }

    private void DataExists(Query queries)
    {


        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(queries, Order.class)
                .build();


        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter
                = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Order model)
            {
                System.out.println("okkkies");
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price: " + model.getPrice() + "$");
                holder.txtProductName.setText(model.getGroupName());
                if(model.getImage() != null)
                    Picasso.get().load(model.getImage().get(0)).into(holder.CartImage);

                orderid_list.add(model.getOrderID());
                group_list.add(model.getGroupName());

                holder.DeleteButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        cartListRef.child(model.getOrderID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    System.out.println(Integer.toString(countCards));
                                    countCards--;
                                    Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                    if(countCards == 0)
                                    {
                                        Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

                        final DatabaseReference cartListRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(model.getGroupName()).child("Orders").child(Prevalent.currentOnlineUser);
                        cartListRef2.child(model.getOrderID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    System.out.println(Integer.toString(countCards));
                                    countCards--;
                                    Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                    if(countCards == 0)
                                    {
                                        Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

                    }
                });

                holder.EditButton.setOnClickListener(new View.OnClickListener()
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
                });

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
