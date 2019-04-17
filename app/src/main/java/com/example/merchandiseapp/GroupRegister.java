package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class GroupRegister extends AppCompatActivity {

    EditText grpName;
    EditText grpContact;
    String[] Locations;
    EditText Address1;
    EditText Address2;
    EditText upi;
    TextView grpEmail;
    ImageView grpProfile;
    Button update;
    Button choose;
    Button upload;
    int flag = 0;

    DatabaseReference GroupData;
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = GroupRegister.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_register);

        grpName = findViewById(R.id.Grp_name);
        grpContact = findViewById(R.id.admin_contact);
        Address1 = findViewById(R.id.admin_address_1);
        Address2 = findViewById(R.id.admin_address_2);
        upi = findViewById(R.id.grp_upi);
        grpEmail = findViewById(R.id.admin_email);

        grpEmail.setText("aayus170101034@iitg.ac.in");

        grpProfile = findViewById(R.id.groupPic);
        update = findViewById(R.id.Grp_update);
        choose = findViewById(R.id.Grp_chooseBtn);
        upload = findViewById(R.id.Grp_uploadBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimage();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(validate_entries()) update_info();
            }
        });


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
               // global.setBitmap(bitmap);
                grpProfile.setImageBitmap(bitmap);
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
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/groups/"+grpEmail.getText().toString().hashCode());

            Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_LONG).show();

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            flag = 1;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(GroupRegister.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public boolean validate_entries(){

        if(grpName.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the name of the group",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(grpContact.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the contact no. of the admin",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(upi.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the upi for the grp",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Address1.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the 1st Location for the grp",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Address2.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the 2nd Location for the grp",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(flag == 0){
            Toast.makeText(getApplicationContext(),"First add Image",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void update_info(){
        GroupNode groupInfo = new GroupNode(grpName.getText().toString().trim(),
                                            grpContact.getText().toString().trim(),
                                            grpEmail.getText().toString().trim(),
                                            upi.getText().toString().trim());

        GroupData = FirebaseDatabase.getInstance().getReference().child("Group");
        GroupData.child(grpName.getText().toString().trim()).child("Details").setValue(groupInfo);
        GroupData.child(grpName.getText().toString().trim()).child("Details").child("Locations").child("0").setValue(Address1.getText().toString().trim());
        GroupData.child(grpName.getText().toString().trim()).child("Details").child("Locations").child("1").setValue(Address1.getText().toString().trim());

        Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();

    }
}
