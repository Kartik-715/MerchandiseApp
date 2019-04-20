package com.example.merchandiseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;

import java.util.List;

public class GroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    G_var global;
    private static final String TAG = GroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);


        global = (G_var) getApplicationContext();

        global.setUsername("CSEA");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView email = headerView.findViewById(R.id.grp_textView);
        email.setText(global.getEmail());

        ImageView imageView = headerView.findViewById(R.id.grp_imageView);
        imageView.setImageBitmap(global.getBitmap());
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
        getMenuInflater().inflate(R.menu.vendor, menu);
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

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(GroupActivity.this,GroupManageProfile.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);
        }  else if (id == R.id.nav_request) {
            Intent intent = new Intent(GroupActivity.this,PreBookings.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);

        } else if (id == R.id.nav_add_item) {

            Intent intent = new Intent(GroupActivity.this,AddMerchandise.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);

        }else if (id == R.id.nav_ViewMerchandise) {

            Intent intent = new Intent(GroupActivity.this,ViewMerchandise.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);

        }

        else if (id == R.id.nav_orders) {

            Intent intent = new Intent(GroupActivity.this,ViewOrder.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);

        }else if (id == R.id.nav_managedMembers) {

            Intent intent = new Intent(GroupActivity.this,ManageMembersActivity.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);

        }
        else if (id == R.id.nav_members) {

            Intent intent = new Intent(GroupActivity.this,AddMembersActivity.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);

        }else if (id == R.id.nav_authorizedMembers) {

            Intent intent = new Intent(GroupActivity.this,AccessedMembersActivity.class);
            intent.putExtra("GroupName",global.getUsername());
            startActivity(intent);

        }
        else if (id == R.id.nav_signout) {

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
                Log.d(TAG, "User at this position does not exist: " + e.toString());
            }
            Intent intent = new Intent(GroupActivity.this, OutlookLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
