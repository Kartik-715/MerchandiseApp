package com.example.merchandiseapp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class myAdaptor extends RecyclerView.Adapter<myAdaptor.MyViewHolder> {

    private Context mContext ;
    private List<Merchandise> mData;

    private itemClickListener listener;

    public myAdaptor(Context mContext, List<Merchandise>lst) {

        this.mContext = mContext;
        this.mData= lst;
        System.out.println("Size of myItems: "+ mData.size());

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_list,parent,false);
        return (new MyViewHolder(view));
    }

    public void setitemClickListener(itemClickListener listener)
    {
        this.listener=listener;
    }


    public void onClick(View view)
    {
        listener.onClick(view,1,false);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //holder.ImageProduct.setImageBitmap();
        holder.price.setText("Rs. "+ mData.get(position).getPrice());
        holder.productName.setText(mData.get(position).getGroupName());
        holder.productDescription.setText(mData.get(position).getCategory());
        if(mData.get(position).getImage() != null)
        {
            Picasso.get().load(mData.get(position).getImage().get(0)).into(holder.ImageProduct);
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick (View view)
//            {
//
//                Intent intent = new Intent( mContext , productDetailActivity.class);
//                intent.putExtra( "brandName", brandName );
//                intent.putExtra( "ImageUrl", imageUrl  );
//                intent.putExtra( "price",  price.get(0));
//                mContext.startActivity(intent);
//
//            }
//
//        }
//
//        );

    }



    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView price;
        ImageView ImageProduct;
        TextView productName;
        TextView productDescription;

        public MyViewHolder(View itemView) {
            super(itemView);
            System.out.println("Setting up View Holder");
            price = itemView.findViewById(R.id.product_price);
            System.out.println(price);
            ImageProduct =itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
        }
    }

}