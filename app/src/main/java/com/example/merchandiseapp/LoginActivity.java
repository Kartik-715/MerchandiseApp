package com.example.merchandiseapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class LoginActivity extends AppCompatActivity {

    private SignInButton mgoogleButton;
    private static final int  RC_SIGN_IN=1;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final String TAG = LoginActivity.class.getSimpleName();
    public G_var global;

    RadioButton keepLogged;
    //SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        global = (G_var) getApplicationContext();

        keepLogged = findViewById(R.id.loggedIn);

// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mgoogleButton = (SignInButton)findViewById(R.id.GoogleBtn);
        mgoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }




    private void signIn() {
        Log.d("Sigin msg","1");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account;
                account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("AUTH msg","qwert2");
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getDisplayName());
                            Log.d(TAG, user.getEmail());

                            checkDatabase(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null,"-1");
                        }

                        // ...
                    }
                });
    }

    private void checkDatabase(final FirebaseUser user){

        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        final DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        UserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // if(keepLogged.isChecked())sp.edit().putBoolean("logged",true);
                if(dataSnapshot.exists()){
                    updateglobals(dataSnapshot,user);
                    updateUI(user,dataSnapshot.child("AccessLevel").getValue().toString());
                }

                else{
                    firstlogin(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(FirebaseUser user,String Access)
    {
        Intent intent = null;
        if(Access.equals("0")) {
            intent = new Intent(getApplicationContext(), HomeActivity.class);
        }
        else if (Access.equals("3")){
            intent = new Intent(getApplicationContext(), Staff.class);
        }
        else if(Access.equals("4")){
            intent = new Intent(getApplicationContext(),grpUser.class);
        }
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void firstlogin(FirebaseUser user){
        Intent intent=new Intent(getApplicationContext(),StartProfileUser.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }


    private void updateglobals(DataSnapshot dataSnapshot,FirebaseUser user)
    {
        global.setUsername(dataSnapshot.child("Name").getValue().toString());
        global.setAddress(dataSnapshot.child("Address").getValue().toString());
        global.setGender(dataSnapshot.child("Gender").getValue().toString());
        global.setContact(dataSnapshot.child("Contact").getValue().toString());
        global.setUid(mAuth.getUid());
        global.setImageRef(FirebaseStorage.getInstance().getReference("images/"+mAuth.getUid()));
        global.setEmail(user.getEmail());

    }

}
