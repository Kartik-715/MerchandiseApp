package com.example.merchandiseapp;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.merchandiseapp.Holder.PreBookingHolder;
import com.example.merchandiseapp.Holder.viewMerchandiseHolder;
import com.example.merchandiseapp.Holder.viewOrderHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ViewOrder extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> orderid_list;
    private DatabaseReference preOederListRef;
    private DatabaseReference myRef;
    private DatabaseReference myRef1;
    private DatabaseReference myRef2;
    private DatabaseReference myRef3;
    private String GroupName;
    private int countCards;
    private HashMap<String, Object> All_orders = new HashMap<String, Object>();
    private String PID;
    private String Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);



        /*Here the groupname has to be extracted*/
        GroupName = getIntent().getStringExtra("GroupName");
//GroupName="CSEA";
        orderid_list = new ArrayList<String>();


        recyclerView = findViewById(R.id.viewOrder_list);
        System.out.println(recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }



    public void makeMerchandisePrivate(View view){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Make Private..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.checked);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure,You want to make the product private and stop taking more requests? ");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if(CheckNetwork.isInternetAvailable(getApplicationContext()))
                {




                myRef1 = FirebaseDatabase.getInstance().getReference("/Group").child(GroupName).child("Merchandise").child(Category).child(PID);
                myRef1.child("IsOpen").setValue("false");

                myRef2 = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(Category).child(PID);
                myRef2.child("IsOpen").setValue("false");
                }
                else{
                    //no connection
                    Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                    toast.show();
                }

                finish();
                startActivity(getIntent());

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        preOederListRef = FirebaseDatabase.getInstance().getReference("/Group").child(GroupName).child("Orders");
        final Query queries = preOederListRef;

        if(CheckNetwork.isInternetAvailable(getApplicationContext()))
        {



        queries.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                countCards = (int) dataSnapshot.getChildrenCount();
                All_orders= (HashMap<String, Object>) dataSnapshot.getValue();
                System.out.println("*******watch out for this***********"+All_orders);
                if(All_orders != null) {

                    if (dataSnapshot.exists()) {
                        //Toast.makeText(CartActivity.this,"data exists",Toast.LENGTH_SHORT).show();
                        DataExists(queries);
                    } else {
                        //Toast.makeText(CartActivity.this,"no data exists",Toast.LENGTH_SHORT).show();
                        NoDataExists();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        }
        else{
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void NoDataExists()
    {
        Toast.makeText(getApplicationContext(),"No Orders Placed as of now.",Toast.LENGTH_LONG);

    }

    private void DataExists(Query queries)
    {


        System.out.println(queries);
        FirebaseRecyclerOptions<OrderDetails> options = new FirebaseRecyclerOptions.Builder<OrderDetails>()
                .setQuery(queries, OrderDetails.class)
                .build();

        if(CheckNetwork.isInternetAvailable(getApplicationContext()))
        {


        final FirebaseRecyclerAdapter<OrderDetails, viewOrderHolder> adapter
                = new FirebaseRecyclerAdapter<OrderDetails,  viewOrderHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull final viewOrderHolder holder, int position, @NonNull final OrderDetails model) {
                System.out.println("*******************+++++*********************"+model.getQuantity());

                final ArrayList<String> qty = model.getQuantity();
                final ArrayList<String> sz = model.getSize();

                long totalquantity = 0;
                for (int i=0;i<qty.size();i++)
                {
                    totalquantity+=Long.parseLong(qty.get(i));
                }
                holder.txtProductQuantity.setText("Total Quantity: " + String.valueOf(totalquantity));
                holder.txtProductPrice.setText("Price: " + model.getPrice());
                holder.txtProductName.setText(model.getCategory()+" : "+model.getPID());

                if (model!=null) {
                    Picasso.get().load(model.getImage()).into(holder.CartImage);
                }

                Spinner dropdown = holder.spinner_qty;
                String[] items = new String[qty.size()];
                for (int i=0;i<qty.size();i++)
                {
                    items[i] = sz.get(i)+" : " + qty.get(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);
                dropdown.setSelection(0);
                //orderid_list.add(model.getOrderid());

                PID = model.getPID();
                Category = model.getCategory();

                System.out.println(PID+model.getPID());

                holder.ReviewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ViewOrder.this, ReviewDisplayActivityGroup.class);
                        intent.putExtra("PID", model.getPID());
                        intent.putExtra("Category", model.getCategory());
                        intent.putExtra("GroupName",GroupName);
                        startActivity(intent);

                    }
                });

                holder.AllOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ViewOrder.this, orderRequestDetails.class);
                        intent.putExtra("PID", model.getPID());
                        intent.putExtra("Category", model.getCategory());
                        intent.putExtra("GroupName",GroupName);
                        startActivity(intent);

                    }
                });



            }

            @NonNull
            @Override
            public viewOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vieworder_list_item, viewGroup, false);
                viewOrderHolder holder = new viewOrderHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        }
        else{
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}