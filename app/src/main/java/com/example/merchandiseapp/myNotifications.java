package com.example.merchandiseapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class myNotifications extends AppCompatActivity {
    private Button myNotification;
    private DatabaseReference notificationRef;
    private int count;
    private DatabaseReference stringNot;
    private String notiId;
    private String val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications);

        //TODO: email to be updated
        String emailForNoti = "gupta170101019@iitg.ac.in";
        int hash = emailForNoti.trim().hashCode();
        final String hashValue = Integer.toString(hash);
        String uidNoti = hashValue;
        notificationRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uidNoti);
        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notiId =  dataSnapshot.getValue(userFetch.class).getNotiList();
               // val = Integer.parseInt(notiId);
                //Toast.makeText(myNotifications.this, "****" + arr[val]  , Toast.LENGTH_SHORT).show();
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                            "YOUR_CHANNEL_NAME",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                    mNotificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Merchandise App:Delivery Status")
                        .setContentText(notiId)
                        .setAutoCancel(true);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                PendingIntent pi = PendingIntent.getActivity(myNotifications.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mNotificationManager.notify(0, mBuilder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
