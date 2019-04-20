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

import java.util.ArrayList;
import java.util.HashMap;

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
                holder.txtProductPrice.setText("Price: Rs" + model.getPrice() );
                holder.txtProductName.setText(model.getGroupName());
                if(model.getImage() != null)
                    Picasso.get().load(model.getImage()).into(holder.RequestImage);

                final int oneTypeProductPrice = ( Integer.valueOf(model.getPrice()) ) * ( Integer.valueOf(model.getQuantity()) ) ;

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

                        final DatabaseReference cartListRef3 = FirebaseDatabase.getInstance().getReference().child("Group").child(model.getGroupName())
                                .child("Requests").child(model.getProductID());

                        final String input_size = model.getSize();

                        cartListRef3.child("Size").addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                final ArrayList<String> size_list = ((ArrayList<String>) dataSnapshot.getValue());
                                for(int j=0;j<size_list.size();j++)
                                {
                                    if(size_list.get(j).equals(input_size))
                                    {
                                        final int idx = j;

                                        cartListRef3.child("Quantity").addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                            {
                                                ArrayList<String> quantity_list = ((ArrayList<String>) dataSnapshot.getValue());
                                                for(int i=0;i<quantity_list.size();i++)
                                                {
                                                    if(i == idx)
                                                    {
                                                        String temp_quantity = quantity_list.get(i);

                                                        int result = Integer.parseInt(temp_quantity);
                                                        int sub_value = Integer.parseInt(model.getQuantity());

                                                        result -= sub_value;
                                                        temp_quantity = Integer.toString(result);
                                                        quantity_list.set(i,temp_quantity);
                                                    }
                                                }

                                                final HashMap<String, Object> requestMap3 = new HashMap<>();
                                                requestMap3.put("Quantity", quantity_list);

                                                cartListRef3.updateChildren(requestMap3).addOnCompleteListener(new OnCompleteListener<Void>()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {
                                                        if(task.isSuccessful())
                                                        {

                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError)
                                            {

                                            }
                                        });

                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });


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


                holder.PayNowButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Prevalent.currentMoney = Integer.toString(oneTypeProductPrice);
                        Intent intent = new Intent(ViewRequestsActivity.this, TakeRequestDetailsActivity.class);
                        intent.putExtra("orderID", model.getOrderID());
                        intent.putExtra("group_name", model.getGroupName());
                        intent.putExtra("product_id", model.getProductID());
                        startActivity(intent);
                    }
                });
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

