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
import java.net.URI;

public class UserRegister extends AppCompatActivity {

    TextView usrEmail;
    EditText usrName;
    EditText usrPhone;
    EditText usrGender;
    EditText usrAddress;
    ImageView usrPic;
    String UID;

    Button choose;
    Button upload;
    Button update;

    int flag = 0;

    String imageLocation;
    JSONObject Juser;

    DatabaseReference UserData;
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = GroupRegister.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        hideNav();

        usrEmail = findViewById(R.id.user_email);
        usrName = findViewById(R.id.user_name);
        usrPhone = findViewById(R.id.user_contact);
        usrGender = findViewById(R.id.user_gender);
        usrPic = findViewById(R.id.userPic);
        usrAddress = findViewById(R.id.user_address);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            String user = (String) b.get("user");
            //   Toast.makeText(getApplicationContext(),"JSON STRING "+ user ,Toast.LENGTH_SHORT).show();
            try{
                Juser = new JSONObject(user);
                //Juser is the required Json object to be used
                //testing to find the user display name
                //     Toast.makeText(getApplicationContext(),Juser.getString("displayName").toString(),Toast.LENGTH_SHORT).show();
                //setting the textview to mail of the logged in user
                usrEmail.setText(Juser.getString("mail"));
                usrName.setText(Juser.getString("displayName"));
            }

            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),"invalid json ",Toast.LENGTH_SHORT).show();
            }
        }

        int temp = usrEmail.getText().hashCode();
        UID = Integer.toString(temp);
        update = findViewById(R.id.user_update);
        choose = findViewById(R.id.user_chooseBtn);
        upload = findViewById(R.id.user_uploadBtn);


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
                usrPic.setImageBitmap(bitmap);
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

            imageLocation = "images/users/"+UID;
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageLocation);

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
                            Toast.makeText(UserRegister.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

        String Gender = usrGender.getText().toString().trim();

        if(usrName.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the name of the usr",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(usrPhone.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the contact no. of the usr",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Gender.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the gender for the usr",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(flag == 0){
            Toast.makeText(getApplicationContext(),"First add Image",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(Gender.equals("male") || Gender.equals("female") || Gender.equals("other"))){
            Toast.makeText(getApplicationContext(),"Check your Gender",Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public void update_info(){
        UserNode userInfo = new UserNode(usrName.getText().toString().trim(),
                usrAddress.getText().toString().trim(),
                usrGender.getText().toString().trim(),
                usrPhone.getText().toString().trim(),
                usrEmail.getText().toString().trim(),
                UID);

        UserData = FirebaseDatabase.getInstance().getReference().child("Users");
        UserData.child(UID).setValue(userInfo);

        Intent i = new Intent(getApplicationContext(),SplashScreen.class);
        i.putExtra("Type","users");
        i.putExtra("Email",usrEmail.getText().toString());
        startActivity(i);
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
