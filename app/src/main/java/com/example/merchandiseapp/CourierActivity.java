package com.example.merchandiseapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CourierActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rv;
    private DatabaseReference ProductRef;
    private List<Order> list= new ArrayList<>();
    private static final String TAG = CourierActivity.class.getSimpleName();
    private HashMap<String, Object> All_orders = new HashMap<String, Object>();
    private HashMap<String, Object> user_order = new HashMap<String, Object>();
    private HashMap<String, Object> Order_Placed = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);


        ProductRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG,"*******sample3**********");
                All_orders= (HashMap<String, Object>) dataSnapshot.getValue();
                System.out.println(All_orders);
                Iterator it1 = All_orders.entrySet().iterator();
                while (it1.hasNext()) {
                    HashMap.Entry pair1 = (HashMap.Entry) it1.next();
                    user_order = (HashMap<String, Object>) pair1.getValue();
                    Iterator it = user_order.entrySet().iterator();
                    while (it.hasNext()) {
                        HashMap.Entry pair = (HashMap.Entry) it.next();

                        System.out.println(pair.getKey() + " = " + pair.getValue());
                        Order_Placed = (HashMap<String, Object>) pair.getValue();

                        System.out.print(Order_Placed.get("isplaced"));
                        if( Order_Placed.get("isplaced").equals("false"))
                        {
                            continue;
                        }

                        else{   Order od = new Order((String) Order_Placed.get("contact"),(String) Order_Placed.get("address"),
                                (String) Order_Placed.get("date"),(String) Order_Placed.get("email"),
                                (String) Order_Placed.get("orderid"),(String) Order_Placed.get("uid"),
                                (String) Order_Placed.get("isplaced"),
                                (String) Order_Placed.get("pid"),(String) Order_Placed.get("pname"),(String) Order_Placed.get("price"),
                                (String) Order_Placed.get("quantity"), (String) Order_Placed.get("status"),(String) Order_Placed.get("time"));
                        System.out.println("hello"+od.getPname()+od.getPrice());
                        list.add(od);
                    }
                        it.remove(); // avoids a ConcurrentModificationException
                    }
                    it1.remove();
                }
                rv = (RecyclerView) findViewById(R.id.recycler_menu);
                rv.setLayoutManager(new LinearLayoutManager(CourierActivity.this));
                rv.setAdapter(new CourierAdapter(CourierActivity.this, list));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.courier, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {


        } else if (id == R.id.nav_profile) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
