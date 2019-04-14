package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.InputStream;

public class StartProfileUser extends AppCompatActivity {

    final FirebaseAuth mauth = FirebaseAuth.getInstance();
    FirebaseUser user;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    G_var global;

    DatabaseReference UserData;
    FirebaseStorage storage;
    StorageReference storageReference;

    TextView email;
    EditText contact;
    EditText gender;
    EditText address;
    ImageView profile_pic;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_profile_user);

        user = mauth.getCurrentUser();
        UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        global = (G_var) getApplicationContext();

        email = findViewById(R.id.show_email_start);
        contact = findViewById(R.id.edit_contact_start);
        gender = findViewById(R.id.edit_gender_start);
        address = findViewById(R.id.edit_address_start);
        profile_pic = findViewById(R.id.start_image);
        name = findViewById(R.id.edit_name_start);

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());

        new StartProfileUser.DownloadImageTask(profile_pic).execute(user.getPhotoUrl().toString());

        Button Creator = findViewById(R.id.updateBtn);
        Creator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateInfo();

               Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
               intent.putExtra("user", user);
               startActivity(intent);

            }
        });

        Button Image_changer = findViewById(R.id.image_change);
        Image_changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void updateInfo(){
        UserData.child("Name").setValue(name.getText().toString());
        UserData.child("Contact").setValue(contact.getText().toString());
        UserData.child("Address").setValue(address.getText().toString());
        UserData.child("Gender").setValue(gender.getText().toString());
        Toast.makeText(getApplicationContext(), "Updated Successfully",Toast.LENGTH_SHORT).show();
        uploadimage();

        updateGlobals();

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
        ImageView imageView = findViewById(R.id.start_image);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
//            Picasso.with(this).load(filePath).into(imageView);
        }
    }

    private void uploadimage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+mauth.getUid());

            Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_LONG).show();

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(StartProfileUser.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            UserData.child("ImageAddress").setValue(ref.getDownloadUrl().toString());

                            addImage();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(StartProfileUser.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        }
    }

    public void addImage(){
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference("images/"+mauth.getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);

                        ImageView imageView = findViewById(R.id.start_image);

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

    public void updateGlobals(){
        global.setImageRef(FirebaseStorage.getInstance().getReference("images/"+mauth.getUid()));
        global.setUsername(name.getText().toString());
        global.setUid(user.getUid());
        global.setContact(contact.getText().toString());
        global.setAddress(address.getText().toString());
        global.setGender(gender.getText().toString());
        global.setEmail(user.getEmail());
    }

}
