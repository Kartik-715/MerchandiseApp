package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;

public class AdminNotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtGroupName, txtEmail, txtContact;
    public Button approveButton, removeButton;
    private ItemClickListner itemClickListner;

    public AdminNotificationViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtGroupName = itemView.findViewById(R.id.groupName);
        txtEmail = itemView.findViewById(R.id.email);
        txtContact = itemView.findViewById(R.id.contact);
        approveButton = itemView.findViewById(R.id.approve);
        removeButton = itemView.findViewById(R.id.remove);

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
