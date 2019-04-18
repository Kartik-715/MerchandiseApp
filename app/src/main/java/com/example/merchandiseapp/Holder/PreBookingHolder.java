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

public class PreBookingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    public ImageView CartImage;
    public Button DeleteButton, RequestButton, PrivateButton;
    private ItemClickListner itemClickListner;
    public Spinner spinner_qty;


    public PreBookingHolder(@NonNull View itemView) {
        super(itemView);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        DeleteButton = itemView.findViewById(R.id.cart_remove_item);
        RequestButton = itemView.findViewById(R.id.cart_view_request);
        PrivateButton = itemView.findViewById(R.id.cart_private_button);
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
