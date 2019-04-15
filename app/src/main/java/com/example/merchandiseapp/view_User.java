package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class view_User extends AppCompatActivity {

    List<User> userList;
    ListView listViewUsers;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__user);

        userList=new ArrayList<>();
        databaseUsers= FirebaseDatabase.getInstance().getReference("Users");
        listViewUsers=findViewById(R.id.listViewUsers);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                    String temp;
                    User user;
                    if(userSnapshot.child("AccessLevel").getValue()!=null){
                        temp=userSnapshot.child("AccessLevel").getValue().toString();
                        if(temp.equals("0")){
                            user= userSnapshot.getValue(User.class);
                            userList.add(user);
                        }
                    }


                }

                UserList adapter=new UserList(view_User.this,userList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
