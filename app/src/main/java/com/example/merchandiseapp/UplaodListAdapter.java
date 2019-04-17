package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UplaodListAdapter extends RecyclerView.Adapter<UplaodListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item)
//
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//
//            public ViewHolder(View itemVew)
//            {
//                super(ItemView);
//
//            }


    }

}
