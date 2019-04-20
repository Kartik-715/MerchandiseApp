package com.example.merchandiseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent_Groups;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



public class ManageMembersActivity extends AppCompatActivity {

    private EditText memberName_editTxt;
    private Button searchMemberButton;
    private ListView memberListView;
    private HashMap<String,Object> hmp = new HashMap<>();
    private ArrayList<String> MembersEmail = new ArrayList<String>();
    ArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_members);

        memberName_editTxt = (EditText) findViewById(R.id.editText_mem);
        searchMemberButton = (Button) findViewById(R.id.button_search);
        memberListView = (ListView) findViewById(R.id.listView_members);



        // TODO : FOR TESTING REMOVE ONCE WE HAVE ADDED CURRENTGROUPNAME FROM CODE
        Prevalent_Groups.currentGroupName ="CSEA";


        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Members").child("Email_ID");


        MembersEmail = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MembersEmail);
        memberListView.setAdapter(adapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hmp = (HashMap<String , Object>) dataSnapshot.getValue();

                long ctr=0;

                long count = dataSnapshot.getChildrenCount();

                Iterator it = hmp.entrySet().iterator();
                while (it.hasNext())
                {
                    HashMap.Entry pair = (HashMap.Entry)it.next();
                    MembersEmail.add(pair.getValue().toString());
                    adapter.notifyDataSetChanged();
                    it.remove();
                    ctr++;
                    if (ctr==count)
                    {

                       memberListView.setAdapter(adapter);
                    }
                }


                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });






    }
}
