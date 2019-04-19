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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.merchandiseapp.Holder.viewMerchandiseHolder;
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

public class ViewMerchandise extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference preOederListRef;
    private DatabaseReference myRef1;
    private DatabaseReference myRef2;
    private String GroupName;
    private int countCards;
    private HashMap<String, Object> All_orders = new HashMap<String, Object>();
    private String PID;
    private String Category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_merchandise);




        /*Here the groupname and category has to be extracted*/
        GroupName = "CSEA";
        Category = "Footwear";



        recyclerView = findViewById(R.id.viewMerchandise_list);
        System.out.println(recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void deleteMerchandise(View view,final String PID,final String Category){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Remove..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.checked);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure,You want to remove item and stop taking anymore orders?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {


                myRef1 = FirebaseDatabase.getInstance().getReference("/Group").child(GroupName).child("Merchandise").child(Category);
                myRef1.child(PID).child("IsOpen").setValue("false");

                myRef2 = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(Category);
                myRef2.child(PID).child("IsOpen").setValue("false");

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

    public void makeMerchandisePublic(View view,final String PID,final String Category){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Make Public..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.checked);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure,You want to make the product Public and start taking Orders? ");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                myRef1 = FirebaseDatabase.getInstance().getReference("/Group").child(GroupName).child("Merchandise").child(Category);
                myRef1.child(PID).child("IsOpen").setValue("true");

                myRef2 = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(Category);
                myRef2.child(PID).child("IsOpen").setValue("true");

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

        preOederListRef = FirebaseDatabase.getInstance().getReference("/Group").child(GroupName).child("Merchandise").child(Category);
        final Query queries = preOederListRef.orderByKey();

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

    private void NoDataExists()
    {
        Toast.makeText(getApplicationContext(),"No Merchandise added as of now.",Toast.LENGTH_LONG);
    }

    private void DataExists(Query queries)
    {


        System.out.println(queries);
        FirebaseRecyclerOptions<Merchandise> options = new FirebaseRecyclerOptions.Builder<Merchandise>()
                .setQuery(queries,Merchandise.class)
                .build();

        final FirebaseRecyclerAdapter<Merchandise, viewMerchandiseHolder> adapter
                = new FirebaseRecyclerAdapter<Merchandise, viewMerchandiseHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewMerchandiseHolder holder, int position, @NonNull final Merchandise model) {

                System.out.println("*******************+++++*********************"+model.getQuantity()+model.getIsOpen());

                if (model.getIsOpen().equals("true"))
                {
                    holder.MakePubliceButton.setVisibility(View.INVISIBLE);
                    holder.txtProductStatus.setText("OPEN");
                }
                else
                {
                    holder.DeleteButton.setVisibility(View.INVISIBLE);
                    holder.txtProductStatus.setText("CLOSED");
                }

                final ArrayList<String> qty = model.getQuantity();
                final ArrayList<String> sz = model.getSize();
                final ArrayList<String> img = model.getImage();

                long totalquantity = 0;
                for (int i=0;i<qty.size();i++)
                {
                    totalquantity+=Long.parseLong(qty.get(i));
                }
                holder.txtProductQuantity.setText("Total Quantity = " + String.valueOf(totalquantity));
                holder.txtProductPrice.setText("Price: " + model.getPrice());
                holder.txtProductName.setText(model.getCategory());
                if(img!=null) {
                    Picasso.get().load(img.get(0)).into(holder.CartImage);
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

                holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteMerchandise(v,model.getPID(),model.getCategory());
                    }


                });

                holder.MakePubliceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeMerchandisePublic(view,model.getPID(),model.getCategory());
                    }
                });

                holder.EditMerchandiseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ViewMerchandise.this,EditMerchandise.class);
                        intent.putExtra("PID",model.getPID());
                        intent.putExtra("GroupName",GroupName);
                        intent.putExtra("Category",Category);
                        startActivity(intent);


                    }
                });




            }

            @NonNull
            @Override
            public viewMerchandiseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_merchandise_list_item, viewGroup, false);
                viewMerchandiseHolder holder = new viewMerchandiseHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
