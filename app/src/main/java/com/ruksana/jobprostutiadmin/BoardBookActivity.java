package com.ruksana.jobprostutiadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BoardBookActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private EditText edt1;
    private Button btn;

    private String saveCurrentDate;
    private String saveCurrentTime;
    private String name, time, randomKey;
    private FirebaseFirestore dbroot;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_add);

        // Initialize ActionBar
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add to Board Book"); // Set the title
            actionBar.setDisplayShowHomeEnabled(true); // Show the home button
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the back button
        }

        // Initialize Firebase Firestore
        dbroot = FirebaseFirestore.getInstance();

        // Initialize ProgressDialog
        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Add Photo");
        loadingBar.setMessage("Please wait while adding the serial.");
        loadingBar.setCanceledOnTouchOutside(false);

        // Check network connectivity
        if (!isConnected()) {
            Toast.makeText(this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
        }

        // Initialize views
        edt1 = findViewById(R.id.edt1);
        btn = findViewById(R.id.btn_add);

        // Set click listener for the button
        btn.setOnClickListener(v -> validateInfo());
    }

    private void validateInfo() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        randomKey = saveCurrentDate + "  " + saveCurrentTime;
        name = edt1.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edt1.setError("Please write a name.");
            edt1.requestFocus();
        } else {
            insertData();
        }
    }

    private void insertData() {
        loadingBar.show();

        // Create a map for the data
        Map<String, String> items = new HashMap<>();
        items.put("name", name);
        items.put("data", "#"); // Replace with dynamic value if needed
        items.put("category", "#"); // Replace with dynamic value if needed
        items.put("time", randomKey);
        items.put("page", "0"); // Replace with dynamic value if needed

        // Add data to Firestore
        dbroot.collection("Data")
                .document("board_book")
                .collection("item")
                .document(randomKey)
                .set(items)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BoardBookActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BoardBookActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                });
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go to the previous activity when the back button is clicked
        return super.onSupportNavigateUp();
    }
}