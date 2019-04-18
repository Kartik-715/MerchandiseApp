package com.example.merchandiseapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.merchandiseapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {


    Context context;
    ArrayList<String> Contact;
    ArrayList<String> Email;
    ArrayList<String> Quantity;
    ArrayList<String> Size;
    ArrayList<String> UserName;


    //    Contact = new ArrayList<>();
//    Email = new ArrayList<>();
//    Quantity = new ArrayList<>();
//    Size = new ArrayList<>();
//    UserName = new ArrayList<>();
    private LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<String> Contact,

                         ArrayList<String> Email,
                         ArrayList<String> Quantity,
                         ArrayList<String> Size,
                         ArrayList<String> UserName

    ) {
        this.context = applicationContext;


        this.Contact = Contact;
        this.Email = Email;
        this.Quantity = Quantity;
        this.Size = Size;
        this.UserName = UserName;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return UserName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_list_view, null);
        TextView Contact1 = (TextView) view.findViewById(R.id.Contact);
        TextView Email1 = (TextView) view.findViewById(R.id.Email);
        TextView Quantity1 = (TextView) view.findViewById(R.id.Quantity);
        TextView Size1 = (TextView) view.findViewById(R.id.Size);
        TextView UserName1 = (TextView) view.findViewById(R.id.userName);
        ImageView icon1 = (ImageView) view.findViewById(R.id.icon);

        System.out.println("hi" + UserName);

        Contact1.setText(Contact.get(i));
        Email1.setText(Email.get(i));
        Quantity1.setText(Quantity.get(i));
        Size1.setText(Size.get(i));

        System.out.println("hi" + UserName.size());

        UserName1.setText(UserName.get(i));

        return view;
    }
}
