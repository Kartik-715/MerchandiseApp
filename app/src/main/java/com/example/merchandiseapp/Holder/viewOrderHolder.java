package com.example.merchandiseapp.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.R;

public class viewOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    public ImageView CartImage;
    public Button ReviewButton,AllOrderButton;
    private ItemClickListner itemClickListner;
    public Spinner spinner_qty;


    public viewOrderHolder(@NonNull View itemView) {
        super(itemView);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        ReviewButton = itemView.findViewById(R.id.cart_review_item);
        AllOrderButton = itemView.findViewById(R.id.cart_view_all_order);
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
