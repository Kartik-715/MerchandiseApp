package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity2 extends AppCompatActivity
{
    private EditText InputEmail, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputEmail = (EditText) findViewById(R.id.login_email_input);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });
    }

    private void LoginUser()
    {
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(email, password);
        }
    }

    private void AllowAccessToAccount(final String email, final String password)
    {
        String temp_email = email;
        int temp = email.hashCode();
        final String hashcode = Integer.toString(temp);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(hashcode).exists())
                {
                        if(dataSnapshot.child(hashcode).child("Password").exists())
                        {
                            if(dataSnapshot.child(hashcode).child("Password").getValue().toString().equals(password))
                            {
                                Toast.makeText(LoginActivity2.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity2.this, HomeActivity.class);
                                intent.putExtra("orderType", "1");
                                Prevalent.currentOnlineUser = hashcode;
                                Prevalent.currentEmail = email;
                                startActivity(intent);
                            }

                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity2.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                        {
                            Toast.makeText(LoginActivity2.this, "Please try loggin with Google or Outlook", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                }

                else
                {
                    Toast.makeText(LoginActivity2.this, "Account with this " + email + " do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
