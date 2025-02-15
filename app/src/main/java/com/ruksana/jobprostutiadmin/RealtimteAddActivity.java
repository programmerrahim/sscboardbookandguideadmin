package com.ruksana.jobprostutiadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RealtimteAddActivity extends AppCompatActivity {

    ActionBar actionBar;
    private String question, cdate,modeltest;
    EditText addNameEditText,addModelTestNumber;


    private String saveCurrentDate;
    private String saveCurrentTime;
    private String productRandomKey;

    private ProgressDialog loadingBar;


    DatabaseReference ProductRef;
    Button addButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtimte_add);

        FirebaseApp.initializeApp(this);








        //int action bar
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add to Realtime");


        //add back button
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addNameEditText = findViewById(R.id.addNameId);
        addModelTestNumber = findViewById(R.id.addModelTestNumberId);


        loadingBar = new ProgressDialog(this);

        addButton = findViewById(R.id.addButtonId);




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        Date date = new Date();
        DateFormat dateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        }
        String currentDate = dateFormat.format(date);

        question = addNameEditText.getText().toString();
        modeltest = addModelTestNumber.getText().toString();
        cdate = currentDate;

        if (TextUtils.isEmpty(question)) {
            addNameEditText.setError("Please write Name");
            addNameEditText.requestFocus();
        }
        if (TextUtils.isEmpty(modeltest)) {
            addModelTestNumber.setError("Please write Model test Number");
            addModelTestNumber.requestFocus();
        }else {
            StoreJobInformation();
        }
    }

    private void StoreJobInformation() {

        loadingBar.setTitle("Add Photo");
        loadingBar.setMessage("Please wait while adding the serial.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        productRandomKey = saveCurrentDate + " " + saveCurrentTime;

        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("pid", productRandomKey);
        itemMap.put("question", question);
        itemMap.put("optionA", "#");
        itemMap.put("optionB", "#");
        itemMap.put("optionC", "#");
        itemMap.put("optionD", "#");
        itemMap.put("correctAnswer", "#");
        itemMap.put("modelTestName", modeltest);
        itemMap.put("category", "#");
        itemMap.put("date", cdate);
        ProductRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("data")
                .child("bcs")
                .child(modeltest)
                .child("questions");


        ProductRef.child(productRandomKey).updateChildren(itemMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(RealtimteAddActivity.this, "Serial is added successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(RealtimteAddActivity.this, "Error :" + message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//go previous activity, when back button of  actionbar clicked
        return super.onSupportNavigateUp();
    }
}