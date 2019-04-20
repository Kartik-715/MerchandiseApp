package com.example.merchandiseapp.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.R;

public class OrderStatusHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductStatus, txtProductGroup, txtProductPrice ;
    public ImageView DeliveredImage;
    private ItemClickListner itemClickListner;

    public OrderStatusHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.status_name);
        txtProductStatus = itemView.findViewById(R.id.status_status);
        txtProductGroup = itemView.findViewById(R.id.status_group);
        txtProductPrice = itemView.findViewById(R.id.status_price);
        DeliveredImage = itemView.findViewById(R.id.product_image);
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
