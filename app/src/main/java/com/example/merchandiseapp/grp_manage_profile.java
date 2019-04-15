package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class grp_manage_profile extends AppCompatActivity {
         EditText grp_name;
         TextView grp_email;
         EditText grp_contact;
         EditText grp_address;
         TextView grp_organisation;
         Button grp_update;
         Button grp_chooseImage;
         Button grp_upLoadImage;
         ImageView imageView;
         final FirebaseAuth mAuth = FirebaseAuth.getInstance();
         FirebaseUser user;
    private Uri filePath;
         private final int PICK_IMAGE_REQUEST = 71;

    G_var global;
    DatabaseReference UserData;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp_manage_profile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = mAuth.getCurrentUser();
        global = (G_var) getApplicationContext();
        grp_name = findViewById(R.id.grp_edit_name);
        grp_email= findViewById(R.id.grp_edit_email);
        grp_contact = findViewById(R.id.grp_edit_contact);
        grp_address = findViewById(R.id.grp_edit_address);
        grp_organisation = findViewById(R.id.grp_organisation);
        grp_update = findViewById(R.id.grp_updateBtn);
        grp_chooseImage = findViewById(R.id.grp_chooseBtn);
        grp_upLoadImage = findViewById(R.id.grp_uploadBtn);
        imageView = findViewById(R.id.grp_profilePic);
        load_grp_details();
        grp_update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_grp_info();
            }
        });
        grp_chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_grp_image();
            }
        });
        grp_upLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_grp_image();
            }
        });
    }


    //function to update the grp info into the database and into the global variables
    private void update_grp_info()
    {
        UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(global.getUid());

        try {
            UserData.child("Name").setValue(grp_name.getText().toString());
            global.setUsername(grp_name.getText().toString());

            UserData.child("Contact").setValue(grp_contact.getText().toString());
            global.setContact(grp_contact.getText().toString());

            UserData.child("Address").setValue(grp_address.getText().toString());
            global.setAddress(grp_address.getText().toString());

            UserData.child("Institution").setValue(grp_organisation.getText().toString());
            Toast.makeText(getApplicationContext(), "Updated Successfully",Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }

    }


    //loading group details into the profile sections
    private void load_grp_details()
    {
        try{
            grp_name.setText(global.getUsername());
            grp_email.setText(global.getEmail());
            grp_contact.setText(global.getContact());
            grp_address.setText(global.getAddress());
        }
        catch (Exception ex)
        {
              Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }

    }


    //selecting image from the user device
    private void choose_grp_image()
    {
         try{
             Intent intent = new Intent();
             intent.setType("image/*");
             intent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
         }
         catch (Exception ex)
         {
             Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
         }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.grp_profilePic);

        try{

                if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                        && data != null && data.getData() != null )
                {
                    filePath = data.getData();
                    Picasso.with(this).load(filePath).into(imageView);
                }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void upload_grp_image() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+global.getUid());

            Toast.makeText(getApplicationContext(),"uploading image",Toast.LENGTH_LONG).show();

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded succesfully", Toast.LENGTH_SHORT).show();
                            addImage();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

            change_images();
        }
    }

    public void addImage(){
        StorageReference mImageRef = global.getImageRef();
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);

                        imageView.setMinimumHeight(dm.heightPixels);
                        imageView.setMinimumWidth(dm.widthPixels);
                        imageView.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }
        });
    }

    public void change_images(){

        //adding images in some imageViews
    }
}

