package com.example.merchandiseapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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
import java.security.acl.Group;
import java.util.concurrent.TimeUnit;

public class GroupRegister extends AppCompatActivity {

    EditText grpName;
    EditText grpContact;
    EditText upi;
    TextView grpEmail;
    ImageView grpProfile;
    Button update;
    Button choose;
    Button upload;
    int flag = 0;
    int flag1 = 0;

    String imageLocation;
    JSONObject Juser;

    DatabaseReference GroupData;
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG = GroupRegister.class.getSimpleName();

    private FirebaseAuth fbAuth;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_register);

        grpName = findViewById(R.id.Grp_name);
        grpContact = findViewById(R.id.admin_contact);
        upi = findViewById(R.id.grp_upi);
        grpEmail = findViewById(R.id.admin_email);

        fbAuth = FirebaseAuth.getInstance();
        //grpEmail.setText("aayus170101034@iitg.ac.in");

        grpProfile = findViewById(R.id.groupPic);
        update = findViewById(R.id.Grp_update);
        choose = findViewById(R.id.Grp_chooseBtn);
        upload = findViewById(R.id.Grp_uploadBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Bundle b = getIntent().getExtras();
        if(b!=null){
            String user = (String) b.get("user");
            //   Toast.makeText(getApplicationContext(),"JSON STRING "+ user ,Toast.LENGTH_SHORT).show();

            if(b.getString("email",null) != null)
            {
                grpEmail.setText(b.getString("email",null));
            }
            else
            {
                try{
                    Juser = new JSONObject(user);
                    //Juser is the required Json object to be used
                    //testing to find the user display name
                    //     Toast.makeText(getApplicationContext(),Juser.getString("displayName").toString(),Toast.LENGTH_SHORT).show();
                    //setting the textview to mail of the logged in user

                    grpEmail.setText(Juser.getString("mail").toString());
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"invalid json ",Toast.LENGTH_SHORT).show();
                }

            }


        }

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
                if(validate_entries()) sendCode();
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
            if(grpName.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"First Enter Group Name",Toast.LENGTH_SHORT).show();
                return;
            }

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            imageLocation = "images/groups/"+grpName.getText().toString().trim();
            final StorageReference ref = storageReference.child(imageLocation);

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

        GroupData = FirebaseDatabase.getInstance().getReference().child("Admin").child(grpName.getText().toString().trim());
        GroupData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(flag1 == 0)Toast.makeText(getApplicationContext(),"Group Requested",Toast.LENGTH_SHORT).show();
                    flag1=2;
                }

                else {
                    flag=1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (flag1 == 2){
            return false;
        }

        GroupData = FirebaseDatabase.getInstance().getReference().child("Groups").child(grpName.getText().toString().trim());
        GroupData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(flag1 == 0) Toast.makeText(getApplicationContext(),"Group With Same Name Already Registered",Toast.LENGTH_SHORT).show();
                    flag1=2;
                }
                else {
                    flag=1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (flag1 == 2){
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

        GroupData = FirebaseDatabase.getInstance().getReference().child("Admin");
        GroupData.child(grpName.getText().toString().trim()).setValue(groupInfo);

        GroupData = GroupData.child(grpName.getText().toString().trim());
        int temp = grpEmail.getText().toString().hashCode();

        GroupData.child("UID").setValue(Integer.toString(temp));
        GroupData.child("Image_Location").setValue(imageLocation);
        GroupData.child("isApproved").setValue("No");

        Toast.makeText(getApplicationContext(),"Group Created",Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public void sendCode() {

        String phoneNumber = grpContact.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        callAlertDialog();
                    }
                };
    }

    public void verifyCode(String code) {

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        Toast.makeText(GroupRegister.this,"Verified Done",Toast.LENGTH_SHORT).show();
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(GroupRegister.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GroupRegister.this,"Login Successful",Toast.LENGTH_SHORT).show();
                            update_info();
                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"OTP ENTERED WAS INVALID",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    public void resendCode() {

        String phoneNumber = grpContact.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }

    public void callAlertDialog(){

        final View view = getLayoutInflater().inflate(R.layout.otp_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupRegister.this);
        builder.setTitle("OTP Verification");
        builder.setCancelable(false);


        final EditText otp = (EditText) view.findViewById(R.id.otp);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(GroupRegister.this,"Login Successful",Toast.LENGTH_SHORT).show();
                verifyCode(otp.getText().toString());
            }
        });
        builder.setNegativeButton("Resend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resendCode();
            }
        }).setCancelable(false);


        builder.setView(view);
        builder.create().show();
    }
}
