package com.example.merchandiseapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;

import static android.app.PendingIntent.getActivity;

public class Add_vendor extends AppCompatActivity {
    EditText username;
    EditText contact;
    EditText password;
    EditText emailid;
    EditText upi;
    EditText address;
    Button btn_add;

    DatabaseReference databaseUserData;
    //  FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);
        //  FirebaseApp.initializeApp(getActivity());

        databaseUserData = FirebaseDatabase.getInstance().getReference().child("Users");
        username = (EditText)findViewById(R.id.UserName);
        contact = (EditText)findViewById(R.id.contact);
        password = (EditText)findViewById(R.id.Pass);
        emailid = (EditText)findViewById(R.id.Email);
        upi = (EditText)findViewById(R.id.upi);
        address = (EditText)findViewById(R.id.address);
        btn_add = (Button)findViewById(R.id.button_staff);

        btn_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addStaff();
            }
        });
    }

    private void addStaff(){
        String Username_str = username.getText().toString().trim();
        String Contact_str = contact.getText().toString().trim();
        String password_str = password.getText().toString().trim();
        String emailid_str = emailid.getText().toString().trim();
        String upi_str = upi.getText().toString().trim();
        String address_str = address.getText().toString().trim();
        if(!TextUtils.isEmpty(Username_str) && !TextUtils.isEmpty(Contact_str) && !TextUtils.isEmpty(password_str) &&
                !TextUtils.isEmpty(emailid_str)  && !TextUtils.isEmpty(upi_str) && !TextUtils.isEmpty(address_str)){
//            Toast.makeText(this,"Yay!!!!!!!!!!!!!!",Toast.LENGTH_LONG).show();
            String id[] = emailid_str.split("@");

            //User_data usr = new User_data(Username_str,Contact_str,password_str,emailid_str,upi_str,"2",address_str);

            //databaseUserData.child(id[0]).setValue(usr);

        }else{
            Toast.makeText(this,"Please fill all the Feilds",Toast.LENGTH_LONG).show();
        }
    }


}



