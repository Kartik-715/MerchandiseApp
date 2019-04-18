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

public class viewMerchandiseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity,txtProductStatus;
    public ImageView CartImage;
    public Button DeleteButton, MakePubliceButton ;
    private ItemClickListner itemClickListner;
    public Spinner spinner_qty;


    public viewMerchandiseHolder(@NonNull View itemView) {
        super(itemView);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductStatus = itemView.findViewById(R.id.product_status);
        DeleteButton = itemView.findViewById(R.id.cart_remove_item);
        MakePubliceButton = itemView.findViewById(R.id.cart_make_public);
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
