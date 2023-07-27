package com.example.project01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText adminIdView;
    EditText adminPassView;
    private final String TAG = "adminActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admint);

        float cornerRadius = 70f; // change this to the desired corner radius
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(cornerRadius);
        shape.setColor(Color.parseColor("#A6DFDA"));

        adminIdView = findViewById(R.id.adminUserName);
        adminPassView = findViewById(R.id.adminPassword);
        adminIdView.setBackground(shape);
        adminPassView.setBackground(shape);

        auth = FirebaseAuth.getInstance();

        Button adminLoginButton = findViewById(R.id.adminLoginButton);
        adminLoginButton.setOnClickListener(view -> {
            String adminID = adminIdView.getText().toString().trim();
            String adminPass = adminPassView.getText().toString().trim();
            if(adminID.equals("pankaj")) adminID = "pankaj.cs.20@nitj.ac.in";
            if(adminPass.equals("123")) adminPass = "123123123123";
            loginUser(adminID, adminPass);
        });

    }

    private void loginUser(String adminID, String adminPass) {
        Log.d(TAG, "Logging trying");
        auth.signInWithEmailAndPassword(adminID, adminPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "Logging Successful");
                Toast.makeText(AdminActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminActivity.this, AdminDashboard.class);
                startActivity(intent);
                Log.d(TAG, "Logging ended");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Logging Failed");
                Toast.makeText(AdminActivity.this, "Admin Login Failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Exception: " + e.getMessage());
                Log.d(TAG, "Logging ended");
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Logging Failed");
                    Toast.makeText(AdminActivity.this, "Admin Login Failed", Toast.LENGTH_SHORT).show();
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.d(TAG, "Exception: " + exception.getMessage());
                    }
                    Log.d(TAG, "Logging ended");
                }
            }
        });
    }

}