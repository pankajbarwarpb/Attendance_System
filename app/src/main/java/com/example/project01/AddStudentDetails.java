package com.example.project01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddStudentDetails extends AppCompatActivity {
    private final String TAG = "Firebase_Database";
    private EditText studentRollNumber, studentBluetoothAddress, studentName, studentBranch, studentBatch, studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_details);

        studentRollNumber = findViewById(R.id.studentRollNumber);
        studentBluetoothAddress = findViewById(R.id.stbtaddr);
        studentName = findViewById(R.id.studentName);
        studentBranch = findViewById(R.id.studentBranch);
        studentBatch = findViewById(R.id.studentBatch);
        studentEmail = findViewById(R.id.studentEmail);

        Button addStudentDetailsButton = findViewById(R.id.addStudentDetailsButton);
        addStudentDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = studentName.getText().toString().trim();
                String btAdd = studentBluetoothAddress.getText().toString();
                String rollnumber =studentRollNumber.getText().toString().trim();
                String branch = studentBranch.getText().toString().trim();
                String batch = studentBatch.getText().toString().trim();
                String email = studentEmail.getText().toString().trim();

                Toast.makeText(AddStudentDetails.this, btAdd, Toast.LENGTH_SHORT).show();

                FirebaseDatabase.getInstance().getReference().child("studentDetails").child(rollnumber).child("Name").setValue(name);
                FirebaseDatabase.getInstance().getReference().child("studentDetails").child(rollnumber).child("Bluetooth Address").setValue(btAdd);
                FirebaseDatabase.getInstance().getReference().child("studentDetails").child(rollnumber).child("Roll Number").setValue(rollnumber);
                FirebaseDatabase.getInstance().getReference().child("studentDetails").child(rollnumber).child("Branch").setValue(branch);
                FirebaseDatabase.getInstance().getReference().child("studentDetails").child(rollnumber).child("Batch").setValue(batch);
                FirebaseDatabase.getInstance().getReference().child("studentDetails").child(rollnumber).child("Email").setValue(email);

                FirebaseDatabase.getInstance().getReference().child("studentDevices").child(rollnumber).setValue(btAdd, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error==null){
                            Log.d(TAG, "added");
                            Toast.makeText(AddStudentDetails.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddStudentDetails.this, "error generated", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "not added");
                        }
                    }
                });
                Toast.makeText(AddStudentDetails.this, "Student registered successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}