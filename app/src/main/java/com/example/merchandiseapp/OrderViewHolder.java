package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    public ImageView CartImage;
    public Button DeleteButton, EditButton;
    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        DeleteButton = itemView.findViewById(R.id.cart_remove_item);
        EditButton = itemView.findViewById(R.id.cart_edit_item);
        CartImage = itemView.findViewById(R.id.product_image);

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

