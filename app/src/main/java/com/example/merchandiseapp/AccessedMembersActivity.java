package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent_Groups;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccessedMembersActivity extends AppCompatActivity
{
    private EditText EmailID;
    private Button AddMember;
    String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessed_members);

        EmailID = findViewById(R.id.Add_Email_Accessed);
        AddMember = findViewById(R.id.Btn_Member_Accessed);

        AddMember.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MemberAdd();
            }
        });
    }


    private void MemberAdd()
    {

        inputText = EmailID.getText().toString();
        inputText = inputText.trim();
        if(inputText.length() == 0)
        {
            System.out.println(inputText);
            Toast.makeText(AccessedMembersActivity.this, "Email ID field can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int idx = 0;
        for(int i=0;i<inputText.length();i++)
        {
            if(inputText.charAt(i) == '@')
            {
                idx = i;
                break;
            }
        }

        if(idx == 0)
        {
            Toast.makeText(AccessedMembersActivity.this, "Please Add Valid Email ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String aux = "";
        for(int i=idx+1;i<inputText.length();i++)
            aux += inputText.charAt(i);

        if(aux.equals("iitg.ac.in") == false)
        {
            Toast.makeText(AccessedMembersActivity.this, "Please Add Valid Email ID", Toast.LENGTH_SHORT).show();
            return;
        }

        //FOR TESTING REMOVE ONCE WE HAVE ADDED CURRENTGROUPNAME FROM CODE
        //Prevalent_Groups.currentGroupName ="CSEA";
        int temp = inputText.hashCode();
        final String hashcode = Integer.toString(temp);
        Toast.makeText(AccessedMembersActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
        final DatabaseReference memberRef2 = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Members");
        final DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Authorized_Members");
        ArrayList<String> members = new ArrayList<>();
        members.add(hashcode);

        for(String member:members)
        {
            memberRef.child("Email_ID").child(member).setValue(inputText);
            memberRef2.child("Email_ID").child(member).setValue(inputText);
        }

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(hashcode))
                {
                    ArrayList<String> groups = new ArrayList<>();
                    groups.add(hashcode);

                    for(String group:groups)
                    {
                        userRef.child(hashcode).child("Authorized_Groups").child(Prevalent_Groups.currentGroupName).setValue("true");
                        userRef.child(hashcode).child("Groups").child(Prevalent_Groups.currentGroupName).setValue("true");
                    }
                }

                else
                {
                    ArrayList<String> groups = new ArrayList<>();
                    groups.add(hashcode);

                    for(String group:groups)
                    {
                        userRef.child(hashcode).child("Authorized_Groups").child(Prevalent_Groups.currentGroupName).setValue("true");
                        userRef.child(hashcode).child("Groups").child(Prevalent_Groups.currentGroupName).setValue("true");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
