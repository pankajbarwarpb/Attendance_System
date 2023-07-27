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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class TeacherActivity extends AppCompatActivity {
    private EditText emailIDView;
    private EditText aadharNoView, facultyID;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        auth = FirebaseAuth.getInstance();

        emailIDView = findViewById(R.id.username);
        aadharNoView = findViewById(R.id.aadharNumber);
        facultyID = findViewById(R.id.facultyID);

        float cornerRadius = 70f; // change this to the desired corner radius

// create a shape drawable with rounded corners and the desired color
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(cornerRadius);
        shape.setColor(Color.parseColor("#A6DFDA"));

// set the shape drawable as the background of the EditText
        emailIDView.setBackground(shape);
        aadharNoView.setBackground(shape);
        facultyID.setBackground(shape);

        Button logInButton = findViewById(R.id.logInButton);

        aadharNoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    aadharNoView.setHint("");
                } else {
                    aadharNoView.setHint("Aadhar Number");
                }
            }
        });
        emailIDView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    emailIDView.setHint("");
                } else {
                    emailIDView.setHint("Official Email ID");
                }
            }
        });


        logInButton.setOnClickListener(view -> {
            String aadhar = aadharNoView.getText().toString().trim();
            String email = emailIDView.getText().toString().trim();
            if(email.equals("pankaj")) email = "pankaj.cs.20@nitj.ac.in";
            if(aadhar.equals("123")) aadhar = "123123123123";
            loginUser(email, aadhar);
        });

    }

    private void loginUser(String email, String aadhar) {
        auth.signInWithEmailAndPassword(email, aadhar).addOnSuccessListener(authResult -> {
            Toast.makeText(TeacherActivity.this, "Teacher Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TeacherActivity.this, TeacherDashboard.class);
            String username = facultyID.getText().toString().trim();
            intent.putExtra("username", username);
            startActivity(intent);
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TeacherActivity.this, "Teacher Login Failed", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "Teacher Login Failed: " + e.getMessage());
                    }
                });
    }
}