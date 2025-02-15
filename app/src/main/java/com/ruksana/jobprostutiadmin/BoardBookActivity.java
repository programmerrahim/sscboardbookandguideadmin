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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BoardBookActivity extends AppCompatActivity {

    ActionBar actionBar;
    EditText edt1;
    Button btn;


    private String saveCurrentDate;
    private String saveCurrentTime;

    private String name, time, randomKey, data, category,page;
    FirebaseFirestore dbroot;


    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_add);

        //int action bar
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add to Board Book");


        //add back button
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FirebaseApp.initializeApp(this);

        loadingBar = new ProgressDialog(this);

        if (!isConnected()) {
            Toast.makeText(this, "No Data Connected!!", Toast.LENGTH_SHORT).show();
        }

        edt1 = findViewById(R.id.edt1);

        btn = findViewById(R.id.btn_add);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInfo();
            }
        });

    }

    private void validateInfo() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        randomKey = saveCurrentDate + "  " + saveCurrentTime;

        dbroot = FirebaseFirestore.getInstance();

        name = edt1.getText().toString().trim();

        time = randomKey.trim();

        data = "#";
        category = "#";
        page = "0";

        if (TextUtils.isEmpty(name)) {
            edt1.setError("Please write  name.");
            edt1.requestFocus();
        } else {
            insertData();
        }
    }


    public void insertData() {

        loadingBar.setTitle("Add Photo");
        loadingBar.setMessage("Please wait while adding the serial.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Map<String, String> items = new HashMap<>();
        items.put("name", name);
        items.put("data", data);
        items.put("category", category);
        items.put("time", randomKey);
        items.put("page", page);

        // Add a new document with a generated ID
        dbroot.collection("Data")
                .document("board_book")
                .collection("item")
                .document(randomKey)
                .set(items)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BoardBookActivity.this, "Data is Inserted..", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BoardBookActivity.this, "An error occurred..Please try again later", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                });


    }

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
        onBackPressed();//go previous activity, when back button of  actionbar clicked
        return super.onSupportNavigateUp();
    }

}