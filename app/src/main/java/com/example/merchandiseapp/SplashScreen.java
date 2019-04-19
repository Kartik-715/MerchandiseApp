package com.example.merchandiseapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    G_var global;
    String type;
    String uid;
    String grpName;
    String email;
    Intent i;
    private static int SPLASH_TIME_OUT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideNav();

        global = (G_var) getApplicationContext();

        Bundle b = getIntent().getExtras();
        if(b!=null){
            //Toast.makeText(getApplicationContext(),"bundle non null",Toast.LENGTH_SHORT).show();
            type = (String) b.get("Type");
            email = (String) b.get("Email");
            int temp = email.trim().hashCode();
            uid = Integer.toString(temp);
            global.setUid(uid);
            //Toast.makeText(getApplicationContext(), (String)b.get("uid")+" " +this.uid,Toast.LENGTH_LONG).show();

            if(type.equals("users")){
                DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(global.getUid());
                UserData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        global.setUsername(dataSnapshot.child("Name").getValue().toString());
                        global.setAddress(dataSnapshot.child("Address").getValue().toString());
                        global.setContact(dataSnapshot.child("Contact").getValue().toString());
                        global.setEmail(dataSnapshot.child("Email_ID").getValue().toString());
                        global.setGender(dataSnapshot.child("Gender").getValue().toString());
                        getImage();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            else{
                grpName = (String) b.get("Name");
                DatabaseReference GrpData = FirebaseDatabase.getInstance().getReference().child("Group").child(grpName).child("Details");
                GrpData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        global.setUsername(grpName);
                        global.setImageLocation(dataSnapshot.child("Image Location").getValue().toString());
                        global.setEmail(dataSnapshot.child("EmailID").getValue().toString());
                        global.setContact(dataSnapshot.child("Contact").getValue().toString());
                        global.setUPI(dataSnapshot.child("UPI").getValue().toString());
                        global.setImageLocation(dataSnapshot.child("Image Location").getValue().toString());
                        getImage();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(type.equals("users"))
                {
                    i = new Intent(getApplicationContext(),HomeActivity.class);
                    i.putExtra("orderType", "1");

                }
                else
                {
                    i = new Intent(getApplicationContext(),GroupActivity.class);
                }
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    public void onResume(){
        super.onResume();
        hideNav();
    }

    public void getImage(){
        StorageReference mImageRef;

        if(type.equals("users")) mImageRef = FirebaseStorage.getInstance().getReference().child("images/users/"+uid);
        else mImageRef = FirebaseStorage.getInstance().getReference().child(global.getImageLocation());
        final long ONE_MEGABYTE = 1024 * 1024 * 20;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Toast.makeText(getApplicationContext(),"Setting Bitmap",Toast.LENGTH_SHORT).show();
                        global.setBitmap(bm);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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


}
