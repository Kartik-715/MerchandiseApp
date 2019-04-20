package com.example.merchandiseapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent_Groups;
import com.facebook.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RedirectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String UID = "2079291549";
    TextView Welcome;
    String email;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RelativeLayout rel;
    Spinner areaSpinner;
    Button home;
    String selected;

    String user;
    JSONObject Juser;
    Bundle b ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);
        hideNav();
        Welcome = findViewById(R.id.welcome);
        rel = findViewById(R.id.part4);
        radioButton1 = findViewById(R.id.user);
        radioButton2 = findViewById(R.id.Group);
        areaSpinner = (Spinner) findViewById(R.id.redirect_spinner);
        home = findViewById(R.id.home_redirect);

        //getting user details from json passed by outlook login................
        b = getIntent().getExtras();
        if(b != null)
        {
            if(b.getString("email", null) != null)
            {
                email = b.getString("email");
                Welcome.setText("Welcome " + email);
                System.out.println("Email is: " + email);
            }
            else
            {

                user = (String) b.get("user");
                //   Toast.makeText(getApplicationContext(),"JSON STRING "+ user ,Toast.LENGTH_SHORT).show();
                try{
                    Juser = new JSONObject(user);
                    Welcome.setText("Welcome    " + Juser.getString("displayName").toString());
                    email = Juser.getString("mail").toString();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"invalid json ",Toast.LENGTH_SHORT).show();
                }

            }


        }

        Button button = findViewById(R.id.submitBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = email.hashCode();
                UID = Integer.toString(temp);
                Toast.makeText(getApplicationContext(),UID,Toast.LENGTH_SHORT).show();
                if(radioButton2.isChecked()) redirect_group();
                else redirect_user();


            }
        });

        areaSpinner.setOnItemSelectedListener(this);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SplashScreen.class);
                i.putExtra("Type","groups");
                i.putExtra("Email",email);
                i.putExtra("Name",selected);
                Prevalent_Groups.currentGroupName = selected ;
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        hideNav();
        home.setVisibility(View.INVISIBLE);
        rel.setVisibility(View.INVISIBLE);
        areaSpinner.setAdapter(null);
    }
    public void hideNav(){
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
        selected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_SHORT).show();
    }


    public void redirect_group()
    {
        Toast.makeText(getApplicationContext(),"yo yo",Toast.LENGTH_SHORT).show();
        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Group");

        UserData.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int flag =0;
                String areaname;
                final List<String> areas = new ArrayList<String>();
                for (DataSnapshot authorizedSnapshot: dataSnapshot.getChildren()) {
                        if (authorizedSnapshot.child("Authorized_Members").child("Email_ID").child(UID).exists()){
                            flag++;
                            areaname = authorizedSnapshot.child("Details").child("GroupName").getValue().toString();
                            areas.add(areaname);
                        }
                }
                if(flag!=0) {
                    areaSpinner.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(RedirectActivity.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areasAdapter);
                    rel.setVisibility(View.VISIBLE);
                    home.setVisibility(View.VISIBLE);
                }

                if(flag == 0){
                    Toast.makeText(getApplicationContext(),"No Such User Found",Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(RedirectActivity.this);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"Group Register",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),GroupRegister.class);
                            if(b.getString("email", null) != null)
                            {
                                email = b.getString("email");
                                intent.putExtra("email",email) ;
                            }
                            else
                            {
                                intent.putExtra("user",Juser.toString());
                            }
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Cancel",null).setCancelable(false);
                    builder.setTitle("Send request for a new Group??");
                    builder.create().show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Fatal Error!!! Try Again",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void redirect_user(){
        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);

        UserData.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(),"Moving to Home Activity",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),SplashScreen.class);
                    i.putExtra("Type","users");
                    i.putExtra("Email",email);
                    startActivity(i);
                }

                else{
                    Toast.makeText(getApplicationContext(),"No Such User Found",Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(RedirectActivity.this);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"User Register",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),UserRegister.class);
                            intent.putExtra("user",Juser.toString());
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Cancel",null).setCancelable(false);
                    builder.setTitle("Shall We Register You??");
                    builder.create().show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Fatal Error!!! Try Again",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
