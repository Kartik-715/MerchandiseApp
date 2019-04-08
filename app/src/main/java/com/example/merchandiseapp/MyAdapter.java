package com.example.merchandiseapp;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext ;
    private String imageUrl;
    private String brandName;
    private String category;
    private itemClickListener listener;

    public MyAdapter(Context mContext, String url, String brandname, String category) {

        System.out.println("HELLO"+brandname);
        this.mContext = mContext;
        this.imageUrl = url;
        this.brandName = brandname;
        this.category =category;
        System.out.println("HELLO1"+brandName);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_list,parent,false);
        // click listener here
        return new MyViewHolder(view);
    }

    public void setitemClickListener(itemClickListener listener)
    {
        this.listener=listener;
    }


    public void onClick(View view)
    {
        //listener.onClick(view,getAdapterPosition(),false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //holder.ImageProduct.setImageBitmap();
        //holder.price.setText();
        System.out.println("HELLO"+brandName);

        holder.productName.setText(brandName);
        holder.productDescription.setText(category);
        Picasso.get().load(imageUrl).into(holder.ImageProduct);

        // load image from the internet using Glide
        //Glide.with(mContext).load(mData.get(position).getImage_url()).apply(options).into(holder.AnimeThumbnail);

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView price;
        ImageView ImageProduct;
        TextView productName;
        TextView productDescription;

        public MyViewHolder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price_textview);
            ImageProduct =itemView.findViewById(R.id.item_image_view);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
        }
    }

}