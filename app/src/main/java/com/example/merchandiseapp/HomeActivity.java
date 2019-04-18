package com.example.merchandiseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProductsRef;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Button btnLogout;
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public ImageView imageView;
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    G_var global;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    View headerView;
    private String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //global = (G_var) getApplicationContext();

        /* Tab Layout Setting */
        flag = getIntent().getStringExtra("flag");
        Prevalent.currentFlag = flag;


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager_id);
        final ViewPagerAdaptor adaptor = new ViewPagerAdaptor(getSupportFragmentManager());

        DatabaseReference allMerchandise;
        allMerchandise = FirebaseDatabase.getInstance().getReference().child("Group").child("CSEA").child("Merchandise");


        allMerchandise.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                HashMap<String, Object> All_merchandise = (HashMap<String, Object>) dataSnapshot.getValue();

                for (Object o : All_merchandise.entrySet())
                {
                    HashMap.Entry p1 = (HashMap.Entry) o;
                    FragmentItem fragment = new FragmentItem();

                    Bundle bundle = new Bundle();
                    bundle.putString("category", (String) p1.getKey());
                    fragment.setArguments(bundle);
                    adaptor.AddFragment(fragment, (String) p1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ErrMerchandise", "Couldn't Read All Merchandise");
            }
        });

        viewPager.setAdapter(adaptor);
        tabLayout.setupWithViewPager(viewPager);


        /* Tab Layout Setting */


        //Toast.makeText(getApplicationContext(), global.getUid() + " " + global.getUsername() + " " + global.getContact(), Toast.LENGTH_LONG).show();

        //Log.d("name", global.getUsername());

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

        final FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        Log.d("name", user.getDisplayName());


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        headerView = navigationView.getHeaderView(0);



        String User_ID, User_Email;
        //User_Email = user.getEmail();
        //User_ID = user.getUid();
        User_Email = "mayank@iitg.ac.in";

        String temp_email = User_Email;
        int temp = User_Email.hashCode();
        final String hashcode = Integer.toString(temp);

        Prevalent.currentOnlineUser = hashcode;
        Prevalent.currentEmail = temp_email;


        TextView navUsername = headerView.findViewById(R.id.NameTextView);
        TextView navemail = headerView.findViewById(R.id.emailtextView);
        //navUsername.setText(global.getUsername());
        navemail.setText(User_Email);

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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        else if (id == R.id.wallet)
        {
            Intent intent = new Intent(this, myWallet.class);
            startActivity(intent);
        }
        else if (id == R.id.orders)
        {
            Intent intent = new Intent(this,Order_History.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_share) {
            Intent intent = new Intent(HomeActivity.this, com.example.merchandiseapp.GroupRegisterActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_send)
        {
            Intent intent = new Intent(HomeActivity.this, DeliveredActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_logout)
        {
            Intent intent = new Intent(HomeActivity.this, AddMembersActivity.class);
            startActivity(intent);
        }

        else if(id == R.id.customized_orders)
        {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.putExtra("flag", "0");
            startActivity(intent);
        }

        else if(id == R.id.orders_list)
        {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            intent.putExtra("flag", "1");
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


    public void addImage(){

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        imageView.setMinimumHeight(dm.heightPixels);
        imageView.setMinimumWidth(dm.widthPixels);
        //Toast.makeText(getApplicationContext(),"Adding Image ..",Toast.LENGTH_SHORT).show();
        //imageView.setImageBitmap(global.getBitmap());
    }
}

