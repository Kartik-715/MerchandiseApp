package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Intent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManageProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    View headerView;
    DatabaseReference UserData;
    EditText name;
    EditText address;
    EditText contact;
    TextView email;
    ImageView nav_imageView;
    ImageView content_imageView;
    ImageView profilePic;
    ImageView bar_imageView;
    ProgressDialog progressDialog;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    G_var global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);

        global = (G_var) getApplicationContext();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);

        name = findViewById(R.id.edit_name_ui);
        address = findViewById(R.id.edit_address_ui);
        contact = findViewById(R.id.edit_contact_ui);
        email = findViewById(R.id.edit_email_ui);

        name.setText(global.getUsername());
        address.setText(global.getAddress());
        contact.setText(global.getContact());
        email.setText(global.getEmail());

        nav_imageView = headerView.findViewById(R.id.imageView);
        content_imageView = findViewById(R.id.profilePic_ui);

        addImage(nav_imageView);
        addImage(content_imageView);

        Prevalent.currentPhone = "";
        Prevalent.currentPhone = contact.getText().toString();
        Prevalent.currentAddress = "";
        Prevalent.currentAddress = address.getText().toString();

//        getInfo();

        Button button = findViewById(R.id.updateBtn_ui);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        Button imageButton1 = findViewById(R.id.chooseBtn_ui);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        Button imageButton2 = findViewById(R.id.uploadBtn_ui);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimage();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setTexts();
    }

    @Override
    protected void onStart(){
        super.onStart();


    }

    @Override
    public void onBackPressed() {
       DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
   @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTexts(){

        TextView navUsername = headerView.findViewById(R.id.NameTextView);

//        TextView profileUsername = findViewById(R.id.profile_name) ;
//        profileUsername.setText(global.getUsername());

        TextView navemail = headerView.findViewById(R.id.emailtextView);
        navUsername.setText(global.getUsername());


        navemail.setText(global.getEmail());

//        TextView Email = findViewById(R.id.edit_email);
//        Email.setText(global.getEmail());

        //Toast.makeText(getApplicationContext(), "Updated Successfully",Toast.LENGTH_SHORT).show();
    }

    public void updateUser()
    {

        UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(global.getUid());

        UserData.child("Name").setValue(name.getText().toString());
        global.setUsername(name.getText().toString());

        UserData.child("Contact").setValue(contact.getText().toString());
        global.setContact(contact.getText().toString());

        UserData.child("Address").setValue(address.getText().toString());
        global.setAddress(address.getText().toString());

        Toast.makeText(getApplicationContext(), "Updated Successfully",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ManageProfile.this, HomeActivity.class);
        Prevalent_Intent.setIntent(intent);
        intent.putExtra("orderType", Prevalent.currentOrderType);
        startActivity(intent);
    }



    private void chooseimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                global.setBitmap(bitmap);
                addImage(content_imageView);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadimage() {

        if(filePath != null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/users/"+global.getUid());

            Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_LONG).show();

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ManageProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            addImage(nav_imageView);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ManageProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
            global.setImageRef(FirebaseStorage.getInstance().getReference("images/"+global.getUid()));
        }
    }


    public void addImage(ImageView imageView)
    {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        imageView.setMinimumHeight(dm.heightPixels);
        imageView.setMinimumWidth(dm.widthPixels);
        imageView.setImageBitmap(global.getBitmap());
    }

}
