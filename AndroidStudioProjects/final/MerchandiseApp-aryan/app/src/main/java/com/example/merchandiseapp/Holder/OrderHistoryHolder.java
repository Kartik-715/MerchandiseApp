package com.example.merchandiseapp.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.Interface.ItemClickListner;
import com.example.merchandiseapp.R;

public class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductQuantity,txtProductPrice, txtProductDeliveryDate,txtOrderDate,txtProductStatus,txtOrderid;
    public ImageView ProductImage;
    private ItemClickListner itemClickListner;

    public OrderHistoryHolder(@NonNull View itemView) {
        super(itemView);
        txtProductQuantity=itemView.findViewById(R.id.product_quantity);
        txtProductPrice=itemView.findViewById(R.id.product_price);
        txtProductName=itemView.findViewById(R.id.product_name);
        txtOrderDate=itemView.findViewById(R.id.order_date);
        txtProductStatus=itemView.findViewById(R.id.product_status);
        txtOrderid=itemView.findViewById(R.id.order_id);
        ProductImage= itemView.findViewById(R.id.product_image);
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
