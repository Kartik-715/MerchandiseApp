package com.example.merchandiseapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.firebase.ui.auth.data.model.PhoneNumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GroupRegisterActivity extends AppCompatActivity
{
    private EditText GroupName,EmailID,Contact;
    private Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_register);

        /*GroupName = findViewById(R.id.Register_Group_Name);
        EmailID = findViewById(R.id.Register_EmailID);
        Contact = findViewById(R.id.Register_Contact);
        Register = findViewById(R.id.Btn_Group_Request);

        EmailID.setText(Prevalent.currentEmail);
        EmailID.setEnabled(false);

        Register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendRequest();
            }
        });*/
    }

   /* private void SendRequest()
    {
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(GroupName.getText().toString());
        final DatabaseReference requestRef2 = FirebaseDatabase.getInstance().getReference().child("Admin");

        requestRef2.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String temp = GroupName.getText().toString();
                if( dataSnapshot.hasChild(temp) )
                {
                    if(GroupName.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(GroupRegisterActivity.this, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(GroupRegisterActivity.this, "Group with This Name already Exists", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    if(GroupName.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(GroupRegisterActivity.this, "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                    }

                    else if(Contact.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(GroupRegisterActivity.this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        final HashMap<String, Object> requestMap = new HashMap<>();
                        requestMap.put("Contact", Contact.getText().toString());
                        requestMap.put("Email", EmailID.getText().toString());
                        requestMap.put("GroupName",GroupName.getText().toString());
                        requestMap.put("isApproved","No");


                        requestRef.updateChildren(requestMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        Toast.makeText(GroupRegisterActivity.this, "Successfully Sent Request to Admin", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }*/
}