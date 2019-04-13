package com.example.merchandiseapp;



import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);

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

