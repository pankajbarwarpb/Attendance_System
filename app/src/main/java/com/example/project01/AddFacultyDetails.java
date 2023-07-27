package com.example.project01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddFacultyDetails extends AppCompatActivity {

    EditText facultyId, facultyName, facultyAadhar, facultyDepartment, facultyBluetoothAddress, facEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty_details);

        facultyId = findViewById(R.id.enterFacultyID);
        facultyName = findViewById(R.id.facultyName);
        facultyAadhar = findViewById(R.id.facultyAadhar);
        facultyDepartment = findViewById(R.id.facultyDepartment);
        facultyBluetoothAddress = findViewById(R.id.facultyBluetoothAddress);
        facEmail = findViewById(R.id.facEmail);

        auth = FirebaseAuth.getInstance();

        Button addNewFaculty = findViewById(R.id.addNewFaulty);
        addNewFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                String password = facultyAadhar.getText().toString().trim();
                String id = facultyId.getText().toString().trim();
                String email = facEmail.getText().toString().trim();
                String name = facultyName.getText().toString();
                String aadhar = facultyAadhar.getText().toString();
                String department = facultyDepartment.getText().toString();
                String btAddr = facultyBluetoothAddress.getText().toString();
                map.put("name", name);
                map.put("aadhar", aadhar);
                map.put("department", department);
                map.put("email", email);
                Toast.makeText(AddFacultyDetails.this, "adding", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("facultyDetails").child(id).child("details").setValue(map);
                FirebaseDatabase.getInstance().getReference().child("facultyDevices").child(id).child("bluetoothaddress").setValue(btAddr);
                registerNewFaculty(email, password);
            }
        });
    }

    private void registerNewFaculty(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddFacultyDetails.this, "Faculty registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(AddFacultyDetails.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}