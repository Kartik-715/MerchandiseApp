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

import com.example.merchandiseapp.Prevalent.Prevalent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class UserSignUp extends AppCompatActivity
{

    EditText usrEmail;
    EditText usrName;
    EditText usrPhone;
    EditText usrGender;
    EditText usrAddress;
    ImageView usrPic;
    EditText usrPassword;
    String usrUID;

    Button choose;
    Button upload;
    Button update;

    int flag = 0;

    String imageLocation;
    JSONObject Juser;

    DatabaseReference UserData;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final String TAG =ManageProfile.class.getSimpleName();

    G_var global;

    private FirebaseAuth fbAuth;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        hideNav();

        usrEmail = findViewById(R.id.user_email_signup);
        usrName = findViewById(R.id.user_name_signup);
        usrPhone = findViewById(R.id.user_contact_signup);
        usrGender = findViewById(R.id.user_gender_signup);
        usrPic = findViewById(R.id.userPic_signup);
        usrAddress = findViewById(R.id.user_address_signup);
        usrPassword = findViewById(R.id.user_password_signup);


        update = findViewById(R.id.user_update_signup);
        choose = findViewById(R.id.user_chooseBtn_signup);
        upload = findViewById(R.id.user_uploadBtn_signup);


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

        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(validate_entries())
                {
                    sendCode();
                }
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        hideNav();
    }

    private void chooseimage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try
            {
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

    private void uploadimage()
    {

        if(filePath != null)
        {
            if(usrEmail.getText().toString().trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Enter Email First",Toast.LENGTH_SHORT).show();
                return;
            }

            int temp = usrEmail.getText().toString().trim().hashCode();
            usrUID = Integer.toString(temp);

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            imageLocation = "images/users/"+usrUID;
            final StorageReference ref = FirebaseStorage.getInstance().getReference().child(imageLocation);

            Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_LONG).show();

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            progressDialog.dismiss();
                            flag = 1;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(UserSignUp.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public boolean validate_entries()
    {

        String Gender = usrGender.getText().toString().trim();

        if(usrEmail.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the email",Toast.LENGTH_SHORT).show();
            return false;
        }

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

        if(usrPassword.getText().toString().trim().length() < 5){
            Toast.makeText(getApplicationContext(),"Check if Password entered is more than 4 chars",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void update_info()
    {
        Prevalent.currentEmail = usrEmail.getText().toString().trim();
        Prevalent.currentOnlineUser = Integer.toString(Prevalent.currentEmail.hashCode());
        Prevalent.currentPhone = usrPhone.getText().toString().trim();
        Prevalent.currentWalletMoney = "0";
        Prevalent.currentAddress = usrAddress.getText().toString().trim();
        Prevalent.currentMoney = "0";

        Toast.makeText(getApplicationContext(),usrEmail.getText().toString().trim() + " " + this.usrUID,Toast.LENGTH_LONG).show();

        UserNode userInfo = new UserNode(usrName.getText().toString().trim(),
                usrAddress.getText().toString().trim(),
                usrGender.getText().toString().trim(),
                usrPhone.getText().toString().trim(),
                usrEmail.getText().toString().trim(),
                usrUID);

        UserData = FirebaseDatabase.getInstance().getReference().child("Users");
        UserData.child(usrUID).setValue(userInfo);
        UserData.child(usrUID).child("Password").setValue(usrPassword.getText().toString().trim());
        UserData.child(usrUID).child("Wallet_Money").setValue("0");

        Intent i = new Intent(getApplicationContext(),SplashScreen.class);
        i.putExtra("Type","users");
        i.putExtra("Email",usrEmail.getText().toString());
        startActivity(i);
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

    public void sendCode() {

        String phoneNumber = usrPhone.getText().toString();

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
        Toast.makeText(UserSignUp.this,"Verified Done",Toast.LENGTH_SHORT).show();
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(UserSignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserSignUp.this,"Login Successful",Toast.LENGTH_SHORT).show();
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

        String phoneNumber = usrPhone.getText().toString();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(UserSignUp.this);
        builder.setTitle("OTP Verification");
        builder.setCancelable(false);


        final EditText otp = (EditText) view.findViewById(R.id.otp);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UserSignUp.this,"Login Successful",Toast.LENGTH_SHORT).show();
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
