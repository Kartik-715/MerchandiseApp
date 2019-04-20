package com.example.merchandiseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminNotificationDisplayActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String uid;
    private RecyclerView.LayoutManager layoutManager;
    //private Button NextProcessBtn;
    private ArrayList<String> groupNameList;
    private DatabaseReference notificationRef;
    private DatabaseReference uidReference;
    //private Button BtnShopNow;
    private TextView notificationEmpty;
    private int countCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification_display);
        groupNameList = new ArrayList<String>();
        recyclerView = findViewById(R.id.notificationList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        notificationEmpty = findViewById(R.id.notiInfo);
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        notificationRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        final Query queries = notificationRef.orderByChild("isApproved").equalTo("No");


        queries.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                countCards = (int) dataSnapshot.getChildrenCount();


                if(dataSnapshot.exists())
                {

                    DataExists(queries);

                }
                else
                {
                    NoDataExists();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void NoDataExists()
    {
        notificationEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void DataExists(Query queries)
    {

        FirebaseRecyclerOptions<Notifications> options = new FirebaseRecyclerOptions.Builder<Notifications>()
                .setQuery(queries, Notifications.class)
                .build();

        FirebaseRecyclerAdapter<Notifications, AdminNotificationViewHolder> adapter
                = new FirebaseRecyclerAdapter<Notifications, AdminNotificationViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull AdminNotificationViewHolder holder, int position, @NonNull final Notifications model)
            {

                holder.txtGroupName.setText( model.getGroupName());
                holder.txtEmail.setText(model.getEmail() );
                holder.txtContact.setText(model.getContact());
                groupNameList.add(model.getGroupName());
                holder.removeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        notificationRef.child(model.getGroupName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    System.out.println(Integer.toString(countCards));
                                    countCards--;
                                    Toast.makeText(AdminNotificationDisplayActivity.this, "Request Rejected Successfully", Toast.LENGTH_SHORT).show();
                                    if(countCards == 0)
                                    {
                                        Intent intent = new Intent(AdminNotificationDisplayActivity.this, AdminNotificationDisplayActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });


                    }


                });

                holder.approveButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        notificationRef.child(model.getGroupName()).child("isApproved").setValue("Yes");
                        uidReference = FirebaseDatabase.getInstance().getReference().child("Users").child("-2119591485");
                        uidReference.child("notiList").setValue("1");
                        Toast.makeText(AdminNotificationDisplayActivity.this, "Request Approved Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notificationlayout, viewGroup, false);

                AdminNotificationViewHolder holder = new AdminNotificationViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



}
