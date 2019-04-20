package com.example.merchandiseapp.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.R;

public class DeliveredViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName, txtProductDeliveryDate;
    public ImageView DeliveredImage;
    public Button Review_Btn;
    private ItemClickListner itemClickListner;

    public DeliveredViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.delivered_name);
        txtProductDeliveryDate = itemView.findViewById(R.id.delivered_date);
        DeliveredImage = itemView.findViewById(R.id.product_image);
        Review_Btn = itemView.findViewById(R.id.Review_Btn);
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
