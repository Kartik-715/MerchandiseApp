package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupManageProfile extends AppCompatActivity {

    TextView name;
    EditText upi;
    EditText location;
    Spinner loc;
    ImageView imageView;

    Button choose;
    Button upload;
    Button update;

    Button addLoc;

    G_var global;

    int flag = 0;
    int flag1 = 0;
    int flag_locationCheck;

    String imageLocation;
    JSONObject Juser;

    DatabaseReference GroupData;
    FirebaseStorage storage;
    StorageReference storageReference;

    List<String> areas = new ArrayList<String>();

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = GroupRegister.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage_profile);

        hideNav();

        global = (G_var) getApplicationContext();

        flag_locationCheck = 0;
        name = findViewById(R.id.grpName);
        location = findViewById(R.id.add_loacation_grp);
        imageView = findViewById(R.id.grpPic);
        upi = findViewById(R.id.edit_upi_grp);
        loc = findViewById(R.id.spinner_loc);
        choose = findViewById(R.id.chooseBtn_grp);
        upload = findViewById(R.id.uploadBtn_grp);
        update = findViewById(R.id.updateBtn_grp);
        addLoc = findViewById(R.id.addBtn_grp);

        imageView.setImageBitmap(global.getBitmap());
        name.setText(global.getUsername());
        upi.setText(global.getUPI());


        DatabaseReference userData = FirebaseDatabase.getInstance().getReference().child("Group").child(name.getText().toString()).child("Details").child("Locations");

        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String areaname;
                    areas= new ArrayList<String>();
                    flag =0;
                    for (DataSnapshot Snapshot : dataSnapshot.getChildren()){
                        flag++;
                        areaname = Snapshot.getValue().toString();
                        areas.add(areaname);
                    }

                    loc.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(GroupManageProfile.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    loc.setAdapter(areasAdapter);
                }

                else loc.setAdapter(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        addLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                update_Loc();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        hideNav();
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
                imageView.setImageBitmap(bitmap);
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

            final StorageReference ref = FirebaseStorage.getInstance().getReference().child(global.getImageLocation());

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
                            Toast.makeText(GroupManageProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        if(upi.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the name of the group",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void update_info(){

        DatabaseReference GroupData = FirebaseDatabase.getInstance().getReference().child("Group").child(name.getText().toString()).child("Details");
        GroupData.child("UPI").setValue(upi.getText().toString());

    }

    public void update_Loc(){
        if(location.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter a Location First",Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference GroupData = FirebaseDatabase.getInstance().getReference().child("Group").child(name.getText().toString()).child("Details").child("Locations");


        if(location_exists(location.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Location already exists",Toast.LENGTH_SHORT).show();
            return;
        }

        if(GroupData == null) Toast.makeText(getApplicationContext(),"hdebhf",Toast.LENGTH_SHORT).show();

        String g = Integer.toString(flag);
        GroupData.child(g).setValue(location.getText().toString());

        location.setText("");
    }

   public boolean location_exists(final String location) {

        if (areas == null) return false;

       for (int i = 0; i < areas.size(); i++)
           if(location.equals(areas.get(i))) return true;

       return false;
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
