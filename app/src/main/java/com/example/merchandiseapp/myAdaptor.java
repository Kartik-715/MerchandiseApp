package com.example.merchandiseapp;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.util.List;

public class myAdaptor extends RecyclerView.Adapter<myAdaptor.MyViewHolder> {

    private Context mContext ;
    private List<Merchandise> mData;
    private ProgressDialog loadingBar;
    private itemClickListener listener;

    public myAdaptor(Context mContext, List<Merchandise>lst, ProgressDialog loadingBar) {

        this.mContext = mContext;
        this.mData= lst;
        this.loadingBar = loadingBar;
        System.out.println("Size of myItems: "+ mData.size());

    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Merchandise> list) {
        mData.addAll(list);
        notifyDataSetChanged();
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

        loadingBar.dismiss();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view)
            {

                Intent intent;
                if(Prevalent.currentOrderType.equals("1"))
                    intent = new Intent( mContext , productDetailActivity.class);
                else
                    intent = new Intent( mContext , RequestDetailActivity.class);

                intent.putExtra( "pid", mData.get(position).getPID() );
                intent.putExtra( "order_id", "empty"  );
                intent.putExtra( "image", mData.get(position).getImage()  );
                intent.putExtra( "category", mData.get(position).getCategory()  );
                intent.putExtra( "groupName", mData.get(position).getGroupName()  );
                intent.putExtra( "size_list",  mData.get(position).getSize());
                mContext.startActivity(intent);

            }
        }

        );
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView price;
        ImageView ImageProduct;
        TextView productName;
        TextView productDescription;

        public MyViewHolder(View itemView)
        {
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