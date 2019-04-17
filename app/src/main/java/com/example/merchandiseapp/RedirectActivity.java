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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RedirectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String UID = "2079291549";
    EditText email;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    Spinner areaSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        email = findViewById(R.id.redirect_email);

        radioButton1 = findViewById(R.id.user);
        radioButton2 = findViewById(R.id.Group);
        areaSpinner = (Spinner) findViewById(R.id.redirect_spinner);


        Button button = findViewById(R.id.submitBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int temp = email.getText().toString().hashCode();
//                UID = Integer.toString(temp);
//                Toast.makeText(getApplicationContext(),UID,Toast.LENGTH_SHORT).show();
                if(radioButton2.isChecked()) redirect_group();
                else redirect_user();
            }
        });

        areaSpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_SHORT).show();
    }


    public void redirect_group(){

        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child("207291549");

        UserData.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> areas = new ArrayList<String>();

                if(dataSnapshot.exists()){
                    for (DataSnapshot areaSnapshot: dataSnapshot.child("Groups").getChildren()) {
                        String areaName = areaSnapshot.getValue(String.class);
                        areas.add(areaName);
                    }

                    areaSpinner.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(RedirectActivity.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areasAdapter);
                }

                else{
                    Toast.makeText(getApplicationContext(),"No Such User Found",Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(RedirectActivity.this);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"Group Register",Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(getApplicationContext(),GroupRegister.class);
                        }
                    });
                    builder.setNegativeButton("Cancel",null).setCancelable(false);
                    builder.setTitle("Create a new Group??");
                    builder.create().show();
                    //Intent intent = new Intent(getApplicationContext(),GroupRegister.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Fatal Error!!! Try Again",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void redirect_user(){
        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child("2079291549");

        UserData.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(),"Moving to Home Activity",Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(),"No Such User Found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Fatal Error!!! Try Again",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
