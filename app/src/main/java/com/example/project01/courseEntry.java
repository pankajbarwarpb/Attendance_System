package com.example.project01;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class courseEntry extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_entry);

        String id = getIntent().getStringExtra("username");

        EditText name = findViewById(R.id.entername);
        EditText code = findViewById(R.id.entercode);
        EditText credit = findViewById(R.id.entercredit);
        EditText branch = findViewById(R.id.enterbranch);
        EditText batch = findViewById(R.id.enterbatch);
        EditText year = findViewById(R.id.enteryear);

        Button cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(courseEntry.this, "Cancelled !!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In CourseEntry.java
                if(name.getText().toString().trim().equals("")) {
                    name.setError("Name is required!");
                    return;
                }

                if(code.getText().toString().trim().equals("")) {
                    code.setError("Code is required!");
                    return;
                }

                if(credit.getText().toString().trim().equals("")) {
                    credit.setError("Credit is required!");
                    return;
                }

                if(branch.getText().toString().trim().equals("")) {
                    branch.setError("Branch is required!");
                    return;
                }

                if(batch.getText().toString().trim().equals("")) {
                    batch.setError("Batch is required!");
                    return;
                }

                if(year.getText().toString().trim().equals("")) {
                    year.setError("Year is required!");
                    return;
                }

                String courseName = name.getText().toString();
                String courseCode = code.getText().toString();
                String courseCredit = credit.getText().toString();
                String courseBranch = branch.getText().toString();
                String courseBatch = batch.getText().toString();
                String courseYear = year.getText().toString();

                HashMap<String, Object> map = new HashMap<>();
                map.put("batch", courseBatch);
                map.put("branch", courseBranch);
                map.put("year", courseYear);
                map.put("code", courseCode);
                map.put("credit", courseCredit);
                map.put("facultyId", id);
                map.put("name", courseName);

                String courseID = id+"_"+code+"_"+branch+"_"+batch+"_"+year;

                Toast.makeText(courseEntry.this, "trying to add course", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("courses").child(courseID).child("details").setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(courseEntry.this, "Course Added !!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(courseEntry.this, "Failed to add course. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
};