package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UplaodListAdapter extends RecyclerView.Adapter<UplaodListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;

    public UplaodListAdapter(List<String> fileNameList, List<String> fileDoneList){
        this.fileNameList=fileNameList;
        this.fileDoneList=fileDoneList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent ,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String fileName = fileNameList.get(position);
        holder.fileNameView.setText(fileName);
        String fileDone = fileDoneList.get(position);

        if(fileDone.equals("uploading"))
        {
            System.out.println("123");
            holder.fileDoneView.setImageResource(R.mipmap.ic_launcher_progress);

        }
        else
        {
            System.out.println("456");

            holder.fileDoneView.setImageResource(R.mipmap.ic_launcher_foreground);
        }

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

            View mview;

            public TextView  fileNameView;
            public ImageView fileDoneView;

            public ViewHolder(View itemView)
            {
                super(itemView);
                mview = itemView;
                fileNameView = (TextView) mview.findViewById(R.id.productText);
                fileDoneView = (ImageView) mview.findViewById(R.id.productImage);
            }

    }

}
