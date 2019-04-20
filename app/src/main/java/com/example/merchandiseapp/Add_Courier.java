/*package com.example.merchandiseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Add_Courier extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__courier);
    }
}
*/

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

public class Add_Courier extends AppCompatActivity {
    EditText username_courier;
    EditText contact_courier;
    EditText password_courier;
    EditText emailid_courier;
    EditText upi_courier;
    EditText address_courier;
    Button btn_add_courier;

    DatabaseReference databaseUserData_courier;
    //  FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__courier);
        //  FirebaseApp.initializeApp(getActivity());

        databaseUserData_courier = FirebaseDatabase.getInstance().getReference().child("Users");
        username_courier = (EditText)findViewById(R.id.UserName);
        contact_courier = (EditText)findViewById(R.id.contact);
        password_courier = (EditText)findViewById(R.id.Pass);
        emailid_courier = (EditText)findViewById(R.id.Email);
        upi_courier = (EditText)findViewById(R.id.upi);
        address_courier = (EditText)findViewById(R.id.address);
        btn_add_courier = (Button)findViewById(R.id.button_staff);

        btn_add_courier.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addCourier();
            }
        });
    }

    private void addCourier(){
        String Username_str = username_courier.getText().toString().trim();
        String Contact_str = contact_courier.getText().toString().trim();
        String password_str = password_courier.getText().toString().trim();
        String emailid_str = emailid_courier.getText().toString().trim();
        String upi_str = upi_courier.getText().toString().trim();
        String address_str = address_courier.getText().toString().trim();
        if(!TextUtils.isEmpty(Username_str) && !TextUtils.isEmpty(Contact_str) && !TextUtils.isEmpty(password_str) &&
                !TextUtils.isEmpty(emailid_str)  && !TextUtils.isEmpty(upi_str) && !TextUtils.isEmpty(address_str)){
            Toast.makeText(this,"Yay!!!!!!!!!!!!!!",Toast.LENGTH_LONG).show();
            String id[] = emailid_str.split("@");

            User_data usr = new User_data(Username_str,Contact_str,password_str,emailid_str,upi_str,"1",address_str);

           databaseUserData_courier.child(id[0]).setValue(usr);
        }else{
            Toast.makeText(this,"Please fill all the Feilds",Toast.LENGTH_LONG).show();
        }
    }
}



