package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;

public class reviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView lreview , lstars, luser, lstatus;
    private ItemClickListner itemClickListner;

    public reviewsViewHolder(@NonNull View itemView)
    {
        super(itemView);
        lreview= itemView.findViewById(R.id.txtReviews);
        lstars= itemView.findViewById(R.id.txtStars);
        luser= itemView.findViewById(R.id.txtUser);
        lstatus = itemView.findViewById(R.id.txtStatus);
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
