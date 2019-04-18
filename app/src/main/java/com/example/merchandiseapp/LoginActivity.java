///*
//package com.example.merchandiseapp;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RadioButton;
//import android.widget.Toast;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.InputStream;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private SignInButton mgoogleButton;
//    private static final int  RC_SIGN_IN=1;
//    private GoogleSignInClient mGoogleSignInClient;
//    private FirebaseAuth mAuth;
//    private static final String TAG = LoginActivity.class.getSimpleName();
//    public G_var global;
//    FirebaseUser user;
//    public FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//    RadioButton keepLogged;
//    ProgressBar progressBar;
//    String final_Access;
//    User_data vendor = null;
//    private Button temp_Button;
//    private Button temp_Button2;
//    private Button temp_Button3, temp_Button4;
//    //SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        temp_Button = findViewById(R.id.btn_add);
//        temp_Button2 = findViewById(R.id.btn_view);
//        temp_Button3 = findViewById(R.id.btn_add_auth);
//        temp_Button4 = findViewById(R.id.btn_unpaid_request);
//
//        global = (G_var) getApplicationContext();
//        keepLogged = findViewById(R.id.loggedIn);
//        progressBar=findViewById(R.id.indeterminateBar);
//// ...
//        temp_Button.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(LoginActivity.this, AddMembersActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        temp_Button2.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        temp_Button3.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(LoginActivity.this, AccessedMembersActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        temp_Button4.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(LoginActivity.this, ViewRequestsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//// Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//        mgoogleButton = (SignInButton)findViewById(R.id.GoogleBtn);
//        mgoogleButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                signIn();
//            }
//        });
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//    }
//
//
//    private void signIn() {
//        Log.d("Sigin msg","1");
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//        progressBar.setVisibility(View.VISIBLE);
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account;
//                account = task.getResult(ApiException.class);
//                assert account != null;
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("AUTH msg","qwert2");
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            user = mAuth.getCurrentUser();
//                            Log.d(TAG, user.getDisplayName());
//                            Log.d(TAG, user.getEmail());
//
//                            checkDatabase(user);
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                           // updateUI(null,"-1");
//                        }
//
//                        // ...
//                    }
//                });
//    }
//
//    private void checkDatabase(final FirebaseUser user){
//
//        String username[] = user.getEmail().split("@");
//
//        final DatabaseReference VendorData = FirebaseDatabase.getInstance().getReference().child("Users").child(username[0]);
//        //final DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        VendorData.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot vendorSnapshot) {
//                // if(keepLogged.isChecked())sp.edit().putBoolean("logged",true);
//                if(vendorSnapshot.exists()){
//                    final_Access = vendorSnapshot.child("AccessLevel").getValue().toString();
//                    updateVendor(vendorSnapshot);
//                }
//
//                else{
//                    final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
//                    //final DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//
//                    UserData.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            // if(keepLogged.isChecked())sp.edit().putBoolean("logged",true);
//                            if(dataSnapshot.exists()){
//                                final_Access = dataSnapshot.child("AccessLevel").getValue().toString();
//                                updateglobals(dataSnapshot,user,final_Access);
//                            }
//
//                            else{
//                                firstlogin(user);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        if(vendor != null){
//            VendorData.removeValue();
//            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).setValue(vendor);
//            firstlogin(user);
//        }
//
//    }
//
//    private void firstlogin(FirebaseUser user){
//        Intent intent=new Intent(getApplicationContext(),StartProfileUser.class);
//        intent.putExtra("user", user);
//        startActivity(intent);
//    }
//
//    private void updateglobals(final DataSnapshot dataSnapshot, final FirebaseUser user, String Access){
//
//        global.setUsername(dataSnapshot.child("Name").getValue().toString());
//        global.setAddress(dataSnapshot.child("Address").getValue().toString());
//        if(Access.equals("0")) global.setGender(dataSnapshot.child("Gender").getValue().toString());
//        global.setContact(dataSnapshot.child("Contact").getValue().toString());
//        global.setUid(mAuth.getUid());
//        global.setImageRef(FirebaseStorage.getInstance().getReference("images/"+mAuth.getUid()));
//        global.setEmail(user.getEmail());
//
//        FirebaseStorage.getInstance().getReference().child("images/"+global.getUid()).getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        StorageReference mImageRef = FirebaseStorage.getInstance().getReference().child("images/"+global.getUid());
//                        final long ONE_MEGABYTE = 1024 * 1024 * 20;
//                        mImageRef.getBytes(ONE_MEGABYTE)
//                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                @Override
//                                public void onSuccess(byte[] bytes) {
//                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                    global.setBitmap(bm);
//
//                                    Intent intent = null;
//                                    if(final_Access.equals("0")) {
//                                        intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                    }
//                                    else if(final_Access.equals("1")) {
//                                        intent = new Intent(getApplicationContext(),CourierActivity.class);
//                                        Toast.makeText(getApplicationContext(),"Open Courier",Toast.LENGTH_LONG).show();
//                                    }
//                                    else if(final_Access.equals("2")) {
//                                        //intent = new Intent(getApplicationContext(),Vendor.class);
//                                        Toast.makeText(getApplicationContext(),"Open Vendor",Toast.LENGTH_LONG).show();
//                                    }
//                                    else if (final_Access.equals("3")){
//                                        intent = new Intent(getApplicationContext(), Staff.class);
//                                    }
//                                    else if(final_Access.equals("4")){
//                                        intent = new Intent(getApplicationContext(),grpUser.class);
//                                    }
//                                    //intent = new Intent(getApplicationContext(),CourierActivity.class);
//
//                                    //intent.putExtra("user", user);
//                                    startActivity(intent);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//
//                                }
//                            });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        new DownloadImageTask(null).execute(user.getPhotoUrl().toString());
//                    }
//        });
//
//
//
//    }
//
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            global.setBitmap(mIcon11);
//            return mIcon11;
//
//        }
//
//        protected void onPostExecute(Bitmap result) {
//
//            Intent intent = null;
//            if(final_Access.equals("0")) {
//                intent = new Intent(getApplicationContext(), HomeActivity.class);
//            }
//            else if(final_Access.equals("1")) {
//                intent = new Intent(getApplicationContext(),CourierActivity.class);
//                Toast.makeText(getApplicationContext(),"Open Courier",Toast.LENGTH_LONG).show();
//                return;
//            }
//            else if(final_Access.equals("2")) {
//                //intent = new Intent(getApplicationContext(),Vendor.class);
//                Toast.makeText(getApplicationContext(),"Open Vendor",Toast.LENGTH_LONG).show();
//                return;
//            }
//            else if (final_Access.equals("3")){
//                intent = new Intent(getApplicationContext(), Staff.class);
//            }
//            else if(final_Access.equals("4")){
//                intent = new Intent(getApplicationContext(),grpUser.class);
//            }
//            //intent = new Intent(getApplicationContext(),CourierActivity.class);
//
//            intent.putExtra("user", user);
//            startActivity(intent);
//        }
//
//    }
//
//    public void updateVendor(DataSnapshot vendorSnapshot){
//
//        vendor = new User_data(
//                vendorSnapshot.child("Name").getValue().toString(),
//                vendorSnapshot.child("Contact").getValue().toString(),
//                vendorSnapshot.child("Password").getValue().toString(),
//                vendorSnapshot.child("EmailID").getValue().toString(),
//                vendorSnapshot.child("UPI").getValue().toString(),
//                vendorSnapshot.child("AccessLevel").getValue().toString(),
//                vendorSnapshot.child("Address").getValue().toString()
//        );
//    }
//}
//
//*/
