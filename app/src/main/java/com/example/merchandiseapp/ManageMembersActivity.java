package com.example.merchandiseapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Groups;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



public class ManageMembersActivity extends AppCompatActivity {

    private EditText memberEmail_editTxt;
    private Button searchMemberButton;
    private ListView memberListView;
    private HashMap<String,Object> hmp = new HashMap<>();
    private ArrayList<String> MembersEmail = new ArrayList<String>();
    private String userEmail;
    ArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_members);

        memberEmail_editTxt = (EditText) findViewById(R.id.editText_mem);
        searchMemberButton = (Button) findViewById(R.id.button_search);
        memberListView = (ListView) findViewById(R.id.listView_members);


        userEmail = Prevalent.currentEmail ;


        System.out.println("Current Group Name: " + Prevalent_Groups.currentGroupName);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Members").child("EmailID");


        MembersEmail = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MembersEmail);
        memberListView.setAdapter(adapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hmp = (HashMap<String , Object>) dataSnapshot.getValue();
                System.out.println(hmp);

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

        memberListView.setTextFilterEnabled(true);

        memberEmail_editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ManageMembersActivity.this.adapter.getFilter().filter(charSequence);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                buttonclickFunc(view,memberEmail_editTxt.getText().toString());



            }
        });



        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ShowToast")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Object o = adapter.getItem(position);
                final String email = o.toString();

                if (email.equals(userEmail))
                {
                    Toast.makeText(getApplicationContext(),"You are already login, so can't remove.", Toast.LENGTH_LONG);
                }

                func(o,email);

            }
        });






    }



    private void buttonclickFunc(View view,final String searchString) {


        if (searchString.equals(""))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Email invalid");
            alertDialogBuilder.setMessage("Enter a valid Email id first");
            alertDialogBuilder.setIcon(R.drawable.checkmark_selected);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else
        {
            ManageMembersActivity.this.adapter.getFilter().filter(searchString);
            adapter.notifyDataSetChanged();

        }

    }

    public void func(final Object o, final String email)
    {
        int hash = email.trim().hashCode();
        final String hashValue = Integer.toString(hash);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Remove..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.checkmark_selected);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure,You want to remove Member?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {



                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Authorized_Members").child("Email_ID");
                final Query queries = ref;

                queries.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        System.out.println(dataSnapshot.getChildrenCount());
                        HashMap<String, Object> AllMembers = (HashMap<String , Object>) dataSnapshot.getValue();

                        Iterator it = AllMembers.entrySet().iterator();

                        long ctr=0;

                        long count = dataSnapshot.getChildrenCount();

                        while (it.hasNext())
                        {
                            ctr++;
                            HashMap.Entry pair = (HashMap.Entry) it.next();
                            if(pair.getValue().equals(email))
                            {
                                alertType2(email,o);
                                //Toast.makeText(CartActivity.this,"data exists",Toast.LENGTH_SHORT).show();

                                break;

                            }
                            if(ctr == count)
                            {
                                RemoveMember(email,o);
                            }
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void alertType2(final String email,final Object o) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Confirm Remove!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.checkmark_selected);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Selected member is authorized member.Are you sure,You want to remove authorized Member?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {


                RemoveAuthorizedMember(email,o);



            }

        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void RemoveMember(final String email,final Object o) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Members").child("Email_ID");
        int hash = email.trim().hashCode();
        final String hashValue = Integer.toString(hash);
        ref.child(hashValue).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    adapter.remove(o);
                    adapter.notifyDataSetChanged();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "Member Removed Successfully.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void RemoveAuthorizedMember(final String email,final Object o) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Authorized_Members").child("Email_ID");
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Group").child(Prevalent_Groups.currentGroupName).child("Members").child("Email_ID");

        int hash = email.trim().hashCode();
        final String hashValue = Integer.toString(hash);
        ref.child(hashValue).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    adapter.remove(o);
                    adapter.notifyDataSetChanged();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "Member Removed Successfully from Authorized list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ref2.child(hashValue).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                    Toast.makeText(getApplicationContext(), "Member Removed Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
