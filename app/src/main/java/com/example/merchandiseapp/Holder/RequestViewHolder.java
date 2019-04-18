package com.example.merchandiseapp.Holder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.R;

public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    public ImageView RequestImage;
    public Button DeleteButton, PayNowButton;
    private ItemClickListner itemClickListner;

    public RequestViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductQuantity = itemView.findViewById(R.id.request_product_quantity);
        txtProductName = itemView.findViewById(R.id.request_product_name);
        txtProductPrice = itemView.findViewById(R.id.request_product_price);
        DeleteButton = itemView.findViewById(R.id.request_remove_item);
        PayNowButton = itemView.findViewById(R.id.request_pay_now);
        RequestImage = itemView.findViewById(R.id.product_image);

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
