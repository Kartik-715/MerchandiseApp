package com.example.merchandiseapp;


        import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
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

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

public class CourierAdapter extends RecyclerView.Adapter<CourierAdapter.MyViewHolder> {

    private Context mContext ;
    private List<Order> mData;
    private ArrayList<Long> price;

    private itemClickListener listener;

    public CourierAdapter(Context mContext, List<Order>lst) {

        this.mContext = mContext;
        this.mData=lst;
        //System.out.println("HELLO1"+mData.get(0).getBrandName() );
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.courier_order_list_item,parent,false);
        // click listener here
        return new MyViewHolder(view);
    }

    public void setitemClickListener(itemClickListener listener)
    {
        this.listener=listener;
    }


    public void onClick(View view)
    {

//        System.out.println("hgello hoeny bunny");
//        Intent intent = new Intent( mContext , productDetailActivity.class);


        listener.onClick(view,1,false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //holder.ImageProduct.setImageBitmap();
        holder.price.setText("Rs."+mData.get(position).getPrice());
        holder.productName.setText(mData.get(position).getGroupName());
        holder.productDescription.setText(mData.get(position).getQuantity());
        holder.productStatus.setText(mData.get(position).getStatus());
        //Picasso.get().load(mData.get(position).getImage()).into(holder.ImageProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick (View view)
        {
            Intent intent = new Intent( mContext , productsCourrier.class );
            intent.putExtra("pid",mData.get(position).getProductID());
            intent.putExtra("pdate",mData.get(position).getDate());
            intent.putExtra("ptime",mData.get(position).getTime());

            mContext.startActivity(intent);
            //Intent intent = new Intent( mContext , productDetailActivity.class);
            //Order objPassed = (Order) mData.get(position);
            //intent.putExtra( "merchandiseObj", objPassed);
            //mContext.startActivity(intent);

        }

                                           }

        );

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView price;
        ImageView ImageProduct;
        TextView productName;
        TextView productDescription;
        TextView productStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price_textview);
            ImageProduct =itemView.findViewById(R.id.item_image_view);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productStatus = itemView.findViewById(R.id.product_status);
        }
    }

}