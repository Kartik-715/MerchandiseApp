package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideNav();

        global = (G_var) getApplicationContext();

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            type = (String) b.get("Type");
            email = (String) b.get("Email");

            int temp = email.trim().hashCode();
            uid = Integer.toString(temp);

            global.setUid(uid);

            if( type.equals("users") )
            {
                DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(global.getUid());
                UserData.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot)
                    {
                        final String usrUID = Integer.toString(email.hashCode());
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                String imageLocation = "images/users/"+usrUID;
                                final StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageLocation);

                                ((StorageReference) ref).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                {
                                    @Override
                                    public void onSuccess(Uri downloadUrl)
                                    {
                                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);

                                        intent.putExtra("orderType", "1");
                                        intent.putExtra("name", dataSnapshot.child("Name").getValue().toString());
                                        intent.putExtra("address", dataSnapshot.child("Address").getValue().toString());
                                        intent.putExtra("contact", dataSnapshot.child("Contact").getValue().toString());
                                        intent.putExtra("email", dataSnapshot.child("Email_ID").getValue().toString());
                                        intent.putExtra("gender", dataSnapshot.child("Gender").getValue().toString());
                                        intent.putExtra("wallet", dataSnapshot.child("Wallet_Money").getValue().toString());
                                        intent.putExtra("image",downloadUrl);
                                        startActivity(intent);
                                        finish();
                                        System.out.println(downloadUrl);
                                    }
                                });

                            }
                        }, SPLASH_TIME_OUT);

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

            else
            {
                grpName = (String) b.get("Name");
                DatabaseReference GrpData = FirebaseDatabase.getInstance().getReference().child("Group").child(grpName).child("Details");

                GrpData.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {


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

       /* new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(type.equals("users"))
                {
                    *//*i = new Intent(getApplicationContext(),HomeActivity.class);
                    i.putExtra("orderType", "1");*//*
                    intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("orderType", "2");

                }

                else
                {
                    intent = new Intent(getApplicationContext(),GroupActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);*/


    }



    @Override
    public void onResume()
    {
        super.onResume();
        hideNav();
    }

    public void getImage()
    {
        StorageReference mImageRef;

        if(type.equals("users"))
            mImageRef = FirebaseStorage.getInstance().getReference().child("images/users/"+uid);

        else
            mImageRef = FirebaseStorage.getInstance().getReference().child(global.getImageLocation());


        final long ONE_MEGABYTE = 1024 * 1024 * 20;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>()
                {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Toast.makeText(getApplicationContext(),"Setting Bitmap",Toast.LENGTH_SHORT).show();
                        global.setBitmap(bm);
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void hideNav()
    {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


}
