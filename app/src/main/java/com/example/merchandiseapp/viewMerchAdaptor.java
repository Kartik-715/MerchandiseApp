package com.example.merchandiseapp;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Groups;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class viewMerchAdaptor extends RecyclerView.Adapter<viewMerchAdaptor.MyViewHolder> {

    private Context mContext ;
    private List<Merchandise> mData;
    private itemClickListener listener;

    public viewMerchAdaptor(Context mContext, List<Merchandise>lst) {

        this.mContext = mContext;
        this.mData= lst;

    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Merchandise> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.view_merchandise_list_item,parent,false);
        return (new MyViewHolder(view));
    }


    public void setitemClickListener(itemClickListener listener)
    {
        this.listener=listener;
    }


    public void onClick(View view)
    {
        listener.onClick(view,1,false);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Merchandise model = mData.get(position) ;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);


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

                Intent intent = new Intent(mContext,EditMerchandise.class);
                intent.putExtra("PID",model.getPID());
                intent.putExtra("GroupName",Prevalent_Groups.currentGroupName);
                intent.putExtra("Category",model.getCategory());
                mContext.startActivity(intent);


            }
        });




    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName, txtProductPrice, txtProductQuantity,txtProductStatus;
        public ImageView CartImage;
        public Button DeleteButton, MakePubliceButton , EditMerchandiseButton;
        private ItemClickListner itemClickListner;
        public Spinner spinner_qty;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
            txtProductName = itemView.findViewById(R.id.cart_product_name);
            txtProductPrice = itemView.findViewById(R.id.cart_product_price);
            txtProductStatus = itemView.findViewById(R.id.cart_product_status);
            DeleteButton = itemView.findViewById(R.id.cart_remove_item);
            MakePubliceButton = itemView.findViewById(R.id.cart_make_public);
            EditMerchandiseButton = itemView.findViewById(R.id.cart_edit_item);
            CartImage = itemView.findViewById(R.id.product_image);
            spinner_qty = itemView.findViewById(R.id.product_quantity_spinner);
        }

        @Override
        public void onClick(View v)
        {
            itemClickListner.onClick(v, getAdapterPosition(), false );
        }

        public void setItemClickListner(ItemClickListner itemClickListner)
        {
            this.itemClickListner = itemClickListner;
        }
    }


    public void deleteMerchandise(View view,final String PID,final String Category){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
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


                DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("/Group").child(Prevalent_Groups.currentGroupName).child("Merchandise").child(Category);
                myRef1.child(PID).child("IsOpen").setValue("false");

                DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(Category);
                myRef2.child(PID).child("IsOpen").setValue("false");

                ((Activity) mContext).finish();
                mContext.startActivity(((Activity) mContext).getIntent());




            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext,"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext,"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void makeMerchandisePublic(View view,final String PID,final String Category){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
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

                DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("/Group").child(Prevalent_Groups.currentGroupName).child("Merchandise").child(Category);
                myRef1.child(PID).child("IsOpen").setValue("true");

                DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference().child("Merchandise").child(Category);
                myRef2.child(PID).child("IsOpen").setValue("true");

                ((Activity) mContext).finish();
                mContext.startActivity(((Activity) mContext).getIntent());
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext,"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext,"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}