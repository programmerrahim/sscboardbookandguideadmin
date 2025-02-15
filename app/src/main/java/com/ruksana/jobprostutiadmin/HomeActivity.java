package com.ruksana.jobprostutiadmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase (only once)
        FirebaseApp.initializeApp(this);

        // Check network connectivity
        if (!isConnected()) {
            Toast.makeText(this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
        }

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Handle Navigation Drawer item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Handle Home click
                Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_profile) {
                // Handle Profile click
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                // Handle Settings click
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
            return true;
        });

        // Enable the hamburger icon and sync the drawer state
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24); // Set your menu icon
        }

        // Set up ActionBarDrawerToggle
        androidx.appcompat.app.ActionBarDrawerToggle toggle = new androidx.appcompat.app.ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Button Click Listeners
        Button boardBook = findViewById(R.id.board_book_id);
        Button addButtonGuideBook = findViewById(R.id.add_button_Guide_book_id);
        Button addButtonHandNote = findViewById(R.id.add_button_hand_note_id);

        boardBook.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BoardBookActivity.class)));
        addButtonGuideBook.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, GuideBookActivity.class)));
        addButtonHandNote.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, HandNoteActivity.class)));
    }

    @Override
    public void onBackPressed() {
        // Close the drawer if it is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Check network connectivity
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}