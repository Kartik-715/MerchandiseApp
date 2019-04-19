package com.example.merchandiseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Intent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ImageView imageView;
    G_var global;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    View headerView;
    private String orderType;
    boolean doubleBackToExitPressedOnce = false;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setting up current User //
            String User_Email = "mayank@iitg.ac.in";
            int temp = User_Email.hashCode();
            final String hashcode = Integer.toString(temp);

            Prevalent.currentOnlineUser = hashcode;
            Prevalent.currentEmail = User_Email;

        // Setting up current User //


        orderType = getIntent().getStringExtra("orderType");
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String contact = getIntent().getStringExtra("contact");
        String email = getIntent().getStringExtra("email");
        String gender = getIntent().getStringExtra("gender");
        String wallet = getIntent().getStringExtra("wallet");
        String image_url = getIntent().getStringExtra("image");

        Prevalent.currentOrderType = orderType;
        Prevalent.currentPhone = contact;
        //Prevalent.currentEmail = email;
        Prevalent.currentEmail = "mayank@iitg.ac.in";
        Prevalent.currentOnlineUser = Integer.toString(Prevalent.currentEmail.hashCode());
        Prevalent.currentWalletMoney = wallet;
        Prevalent.currentGender = gender;
        Prevalent.currentName = name;
        Prevalent.currentAddress = address;
        Prevalent.currentImage = image_url;

        System.out.println("Chigu " + Prevalent.currentOrderType);
        System.out.println("Chigu " + Prevalent.currentPhone);
        System.out.println("Chigu " + Prevalent.currentEmail);
        System.out.println("Chigu " + Prevalent.currentOnlineUser);
        System.out.println("Chigu " + Prevalent.currentWalletMoney);
        System.out.println("Chigu " + Prevalent.currentGender);
        System.out.println("Chigu " + Prevalent.currentName);
        System.out.println("Chigu " + Prevalent.currentAddress);



        /*loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Home Page");
        loadingBar.setMessage("Please wait, while we are Loading Home Page");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();*/

        /* Tab Layout Setting */

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager_id);
        final ViewPagerAdaptor adaptor = new ViewPagerAdaptor(getSupportFragmentManager());


        String uid = Prevalent.currentOnlineUser;

        DatabaseReference userGroups = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Groups");
        userGroups.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                final List<String> accessibleGroups = (List<String>) dataSnapshot.getValue();
                System.out.println(accessibleGroups);

                final String orderType = Prevalent.currentOrderType; // Selecting Order Type //

                FirebaseDatabase.getInstance().getReference().child("Merchandise").addListenerForSingleValueEvent(new ValueEventListener()
                {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        //adaptor.clearFragments();
                        Log.w("DataChanged","Data is Changed") ;

                        for (DataSnapshot postdatasnapshot : dataSnapshot.getChildren())
                        {
                            List<Merchandise> list = new ArrayList<>();
                            for(DataSnapshot merchandise: postdatasnapshot.getChildren())
                            {
                                Merchandise mr = merchandise.getValue(Merchandise.class);
                                if(accessibleGroups != null)
                                {
                                    Set<String> s = new HashSet<>(accessibleGroups);
                                    if (mr.getAccessGroup() != null)
                                    {
                                        Set<String> u = new HashSet<>(mr.getAccessGroup());
                                        s.retainAll(u);
                                        if (s.size() != 0 && mr.getOrderType().equals(orderType))
                                        {
                                            list.add(mr);
                                        }
                                    }
                                }
                            }

                            if(list.size() != 0)
                            {
                                FragmentItem fragment = new FragmentItem() ;
                                Bundle bundle = new Bundle() ; // Incase you want to pass some arguments into Fragment //
                                fragment.setObject(list);
                                fragment.setArguments(bundle);
                                System.out.println("Added a fragment!");
                                adaptor.AddFragment(fragment, (String) postdatasnapshot.getKey() );

                            }

                        }

                        // I have the list now! //



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Log.d("Error", "Couldn't read this Merchandise");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });




        viewPager.setAdapter(adaptor);
        tabLayout.setupWithViewPager(viewPager);

        /******** Tab Layout Setting **********/


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });

//        final FirebaseAuth mauth = FirebaseAuth.getInstance();
//        FirebaseUser user = mauth.getCurrentUser();
//        Log.d("name", user.getDisplayName());


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);



        //String User_ID, User_Email;
        //User_Email = user.getEmail();
        //User_ID = user.getUid();
//        User_Email = "mayank@iitg.ac.in";
//
//        String temp_email = User_Email;
//        int temp = User_Email.hashCode();
//        final String hashcode = Integer.toString(temp);
//
//        Prevalent.currentOnlineUser = hashcode;
//        Prevalent.currentEmail = temp_email;

        /*User_ID = user.getUid();

        Prevalent.currentOnlineUser = User_ID;
        Prevalent.currentEmail = User_Email;*/

        TextView navUsername = headerView.findViewById(R.id.NameTextView);
        TextView navemail = headerView.findViewById(R.id.emailtextView);
        //navUsername.setText(global.getUsername());
        navemail.setText(Prevalent.currentEmail);
        imageView = headerView.findViewById(R.id.imageView);
        //addImage();
//        new DownloadImageTask(imageView)
//                .execute(user.getPhotoUrl().toString());
    }

    /*@Override
    public void onResume() {

        super.onResume();
        imageView = headerView.findViewById(R.id.imageView);
        addImage();

    }*/

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
        {
            if(doubleBackToExitPressedOnce)
            {
                finish();
                moveTaskToBack(true);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.manage_profile)
        {
            Intent intent = new Intent(this, ManageProfile.class);
            startActivity(intent);
        }

        /*else if (id == R.id.wallet)
        {
            Intent intent = new Intent(this, myWallet.class);
            startActivity(intent);
        }*/
        else if (id == R.id.orders)
        {
            Intent intent = new Intent(this,Order_History.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_share)
        {
            Intent intent = new Intent(HomeActivity.this, com.example.merchandiseapp.GroupRegisterActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_send)
        {
            Intent intent = new Intent(HomeActivity.this, DeliveredActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            PublicClientApplication sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    R.raw.auth_config);
                /* Attempt to get a account and remove their cookies from cache */
                List<IAccount> accounts = null;

                try {
                    accounts = sampleApp.getAccounts();

                    if (accounts == null) {
                        /* We have no accounts */

                    } else if (accounts.size() == 1) {
                        /* We have 1 account */
                        /* Remove from token cache */
                        sampleApp.removeAccount(accounts.get(0));
                        //  updateSignedOutUI();

                    }
                    else {
                        /* We have multiple accounts */
                        for (int i = 0; i < accounts.size(); i++) {
                            sampleApp.removeAccount(accounts.get(i));
                        }
                    }

                    Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                            .show();

                } catch (IndexOutOfBoundsException e) {
                    //Log.d(TAG, "User at this position does not exist: " + e.toString());
                }
            Intent intent = new Intent(HomeActivity.this, OutlookLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        else if(id == R.id.customized_orders)
        {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.putExtra("orderType", "2");
            startActivity(intent);
        }

        else if(id == R.id.orders_list)
        {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.putExtra("orderType", "1");
            Prevalent_Intent.setIntent(intent);
            startActivity(intent);
        }

        else if(id == R.id.unpaid_request)
        {
            Intent intent = new Intent(HomeActivity.this, ViewRequestsActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void addImage()
    {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        imageView.setMinimumHeight(dm.heightPixels);
        imageView.setMinimumWidth(dm.widthPixels);
        //Toast.makeText(getApplicationContext(),"Adding Image ..",Toast.LENGTH_SHORT).show();
        //imageView.setImageBitmap(global.getBitmap());
    }





}

