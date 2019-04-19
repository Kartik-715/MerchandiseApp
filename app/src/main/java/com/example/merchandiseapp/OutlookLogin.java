package com.example.merchandiseapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.*;



public class OutlookLogin<Password, Webmail, login_button> extends AppCompatActivity
{
    final static String SCOPES [] = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    G_var global;

    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button LoginButton;
    Button Join;
    FirebaseDatabase database;
    DatabaseReference ref;
    boolean flag = false;
    EditText email,password;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlook_login);
        hideNav();
        callGraphButton = (Button) findViewById(R.id.callGraph);
        LoginButton = (Button) findViewById(R.id.Login);
       // signOutButton = (Button) findViewById(R.id.clearCache);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        Join = findViewById(R.id.JoinNow);

        callGraphButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onCallGraphClicked();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                onLoginButtonClicked();
            }
        });

        Join.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(OutlookLogin.this,UserSignUp.class);
                startActivity(i);
            }
        });

        /* Configure your sample app and save state for this activity */
        sampleApp = null;
        if (sampleApp == null)
        {
            sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    R.raw.auth_config);
         //  if(sampleApp != null) global.setSampleApp(sampleApp);
        }

        List<IAccount> accounts = null;

        try
        {
            accounts = sampleApp.getAccounts();

            if (accounts != null && accounts.size() == 1)
            {
                //sampleApp.acquireTokenSilentAsync(SCOPES, accounts.get(0), getAuthSilentCallback());
            }
            else
            {
                /* We have no account or >1 account */
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.d(TAG, "Account at this position does not exist: " + e.toString());
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        hideNav();
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

    //
    // Core Identity methods used by MSAL
    // ==================================
    // onActivityResult() - handles redirect from System browser
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // onSignOutClicked() - Signs account out of the app & updates UI
    // callGraphAPI() - called on successful token acquisition which makes an HTTP request to graph
    //

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked()
    {
        sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
    }

    private void onLoginButtonClicked()
    {
        String Email = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");

        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password))
        {
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }

        flag = false;
        int hash = Email.hashCode();

        final String hashValue = Integer.toString(hash);

        ref.child(hashValue).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("Password").getValue().toString().equals(Password))
                    {
                        flag = true;
                        Intent intent = new Intent(OutlookLogin.this, SplashScreen.class);
                        intent.putExtra("Type", "users");
                        intent.putExtra("Email", email.getText().toString() );
                        Prevalent.currentEmail = email.getText().toString();
                        Prevalent.currentOnlineUser = Integer.toString(Prevalent.currentEmail.hashCode());
                        startActivity(intent);
                    }

                    else
                        Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                }

                else
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /* Clears an account's tokens from the cache.
     * Logically similar to "sign out" but only signs out of this app.
     */
    /*private void onSignOutClicked() {

        // Attempt to get a account and remove their cookies from cache

        List<IAccount> accounts = null;

        try {
            accounts = sampleApp.getAccounts();

            if (accounts == null) {
                // We have no accounts

            } else if (accounts.size() == 1) {
                // We have 1 account
                // Remove from token cache
                sampleApp.removeAccount(accounts.get(0));
              //  updateSignedOutUI();

            }
            else {
                // We have multiple accounts
                for (int i = 0; i < accounts.size(); i++) {
                    sampleApp.removeAccount(accounts.get(i));
                }
            }

            Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
    }
*/
    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (authResult.getAccessToken() == null) {return;}

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* Successfully called graph, process data and send to UI */
                Log.d(TAG, "Response: " + response.toString());

                updateGraphUI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    @Override


    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
               // .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    //
    // Helper methods manage UI updates
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // updateSuccessUI() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    /* Sets the graph response */
    private void updateGraphUI(JSONObject graphResponse)
    {
        Intent i = new Intent(OutlookLogin.this, RedirectActivity.class);
        i.putExtra("user", graphResponse.toString());

        startActivity(i);
    }
/*     Accessing details of the user from the JSONObject can be done as follows...
       The below code is for retrieving attributes of the object

        String displayName="";
        JSONArray businessPhones;
        String givenName;
        String jobTitle;
        String mail;
        String mobilePhone;
        String officeLocation;
        String preferredLanguage;
        String surname;
        String userPrincipalName;
        String id;
        try{
            displayName = graphResponse.getString("displayName");
            Toast.makeText(getApplicationContext(),displayName,Toast.LENGTH_SHORT).show();

            givenName = graphResponse.getString("givenName");
            Toast.makeText(getApplicationContext(),givenName,Toast.LENGTH_SHORT).show();

            jobTitle = graphResponse.getString("jobTitle");
            Toast.makeText(getApplicationContext(),jobTitle,Toast.LENGTH_SHORT).show();

            mail = graphResponse.getString("mail");
            Toast.makeText(getApplicationContext(),mail,Toast.LENGTH_SHORT).show();

            mobilePhone = graphResponse.getString("mobilePhone");
            Toast.makeText(getApplicationContext(),mobilePhone,Toast.LENGTH_SHORT).show();

            officeLocation = graphResponse.getString("officeLocation");
            Toast.makeText(getApplicationContext(),officeLocation,Toast.LENGTH_SHORT).show();

            preferredLanguage = graphResponse.getString("preferredLocation");
            Toast.makeText(getApplicationContext(),preferredLanguage,Toast.LENGTH_SHORT).show();

            surname = graphResponse.getString("surname");
            Toast.makeText(getApplicationContext(),surname,Toast.LENGTH_SHORT).show();

            userPrincipalName = graphResponse.getString("userPrincipalName");
            Toast.makeText(getApplicationContext(),userPrincipalName,Toast.LENGTH_SHORT).show();

            id = graphResponse.getString("id");
            Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
        }
         catch (Exception ex)
         {
             Toast.makeText(getApplicationContext(),"Exception "+ex.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

         }
*/
    //}

    /* Set the UI for successful token acquisition data */
 /*   private void updateSuccessUI() {
        callGraphButton.setVisibility(View.INVISIBLE);
        signOutButton.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                authResult.getAccount().getUsername());
      //  findViewById(R.id.graphData).setVisibility(View.VISIBLE);
    }*/

    /* Set the UI for signed out account */
   /* private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
      //  findViewById(R.id.graphData).setVisibility(View.INVISIBLE);
        //((TextView) findViewById(R.id.graphData)).setText("No Data");
    }*/

    //
    // App callbacks for MSAL
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity()
    {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */


    /*private AuthenticationCallback getAuthSilentCallback()
    {
        return new AuthenticationCallback()
        {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                *//* Successfully got a token, call graph now *//*
                Log.d("Fuck", "Successfully authenticated");

                *//* Store the authResult *//*
                authResult = authenticationResult;

                *//* call graph *//*
                callGraphAPI();

                *//* update the UI to post call graph state *//*
              //  updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                *//* Failed to acquireToken *//*
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    *//* Exception inside MSAL, more info inside MsalError.java *//*
                } else if (exception instanceof MsalServiceException) {
                    *//* Exception when communicating with the STS, likely config issue *//*
                } else if (exception instanceof MsalUiRequiredException) {
                    *//* Tokens expired or no session, retry with interactive *//*
                }
            }

            @Override
            public void onCancel() {
                *//* User canceled the authentication *//*
                Log.d(TAG, "User cancelled login.");
            }
        };
    }*/

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback getAuthInteractiveCallback()
    {
        return new AuthenticationCallback()
        {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult)
            {
                /* Successfully got a token, call graph now */
                Log.d("Chigu", "Successfully authenticated");
                Log.d("Chigu", "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                //updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception)
            {
                /* Failed to acquireToken */
                Log.d("Chiguu", "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException)
                {
                    /* Exception inside MSAL, more info inside MsalError.java */
                }
                else if (exception instanceof MsalServiceException)
                {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel()
            {
                /* User canceled the authentication */
                Log.d("Chiguu", "User cancelled login.");
            }
        };
    }
}
