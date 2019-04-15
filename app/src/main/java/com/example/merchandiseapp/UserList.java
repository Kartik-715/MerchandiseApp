package com.example.merchandiseapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public UserList(Activity context,List<User> userList){
        super(context,R.layout.list_layout_users,userList);
        this.context=context;
        this.userList=userList;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem =inflater.inflate(R.layout.list_layout_users,null,true);
        TextView userID = listViewItem.findViewById(R.id.userID);
        TextView userName=listViewItem.findViewById(R.id.userName);//

        User user = userList.get(position);
        userID.setText(user.EmailID);

        userName.setText(user.Name);//

        return listViewItem;
    }
}