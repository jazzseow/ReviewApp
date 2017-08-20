package com.example.review;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    private ShareDialog mShareDialog;
    boolean signedInFB, isLocallyEdited, locallyChangedPhoto;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReviewDBHandler dbHandler = new ReviewDBHandler(this);
        dbHandler.insertCategory();

        FacebookSdk.sdkInitialize(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        ImageView navProfilePic = (ImageView) header.findViewById(R.id.nav_profile_pic);
        TextView navUsername = (TextView) header.findViewById(R.id.nav_username);

        pref = getSharedPreferences("account", MODE_PRIVATE);
        signedInFB = pref.getBoolean("signedInAsFB", true);
        isLocallyEdited = pref.getBoolean("locallyEdited", false);
        locallyChangedPhoto = pref.getBoolean("locallyChangedPhoto", false);
        String name = pref.getString("name", "nil");
        String surname = pref.getString("surname", "nil");
        if(signedInFB == true) {
            String imageUrl = pref.getString("imageUrl", "nil");
            if(!locallyChangedPhoto)
                new DownloadImage(navProfilePic).execute(imageUrl);
            else
                navProfilePic.setImageBitmap(BitmapFactory.decodeFile(pref.getString("profilePhoto", "NULL")));
            navigationView.getMenu().getItem(1).setIcon(R.drawable.com_facebook_button_icon_white);
            if(!isLocallyEdited)
                navUsername.setText(name + " " + surname);
            else
                navUsername.setText(pref.getString("editedName", "NULL"));
        } else if (signedInFB == false) {
            navigationView.getMenu().getItem(1).setIcon(R.drawable.common_google_signin_btn_icon_light_disabled);
            if(!locallyChangedPhoto)
                navProfilePic.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
            else
                navProfilePic.setImageBitmap(BitmapFactory.decodeFile(pref.getString("profilePhoto", "NULL")));
            if(!isLocallyEdited)
                navUsername.setText(name + " " + surname);
            else
                navUsername.setText(pref.getString("editedName", "NULL"));
        } else {
            navProfilePic.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNewReview = new Intent(getApplicationContext(), NewReview.class);
                startActivity(createNewReview);
            }

        });

        //Side menu


        mShareDialog = new ShareDialog(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void onFABPressed(View v) {
        //setContentView(R.layout.activity_new_review);
        Intent createNewReview = new Intent(this, NewReview.class);
        startActivity(createNewReview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchMenuItem);
        searchView.setBackgroundColor(Color.WHITE);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.DKGRAY);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.LTGRAY);
        searchView.isSubmitButtonEnabled();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("search",true);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up bordered_box, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.search_settings){
            new SearchSettings().show(getSupportFragmentManager(),"Search Settings");
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_settings) {
            Intent intent = new Intent(this, UpdateProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout){
            pref = getSharedPreferences("account", MODE_PRIVATE);
            pref.edit().clear().commit();
            if(signedInFB)
                logoutFB();
            else
                logoutGoogle();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
    }

    public void logoutFB(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    private void logoutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                });
    }
}
