package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class StudentActivity extends AppCompatActivity {
    private EditText userEmail;
    private EditText rollNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        userEmail = findViewById(R.id.emailID);
        rollNumber = findViewById(R.id.rollnumber);

        float cornerRadius = 70f; // change this to the desired corner radius

// create a shape drawable with rounded corners and the desired color
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(cornerRadius);
        shape.setColor(Color.parseColor("#A6DFDA"));

// set the shape drawable as the background of the EditText
        userEmail.setBackground(shape);
        rollNumber.setBackground(shape);

        userEmail.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                userEmail.setHint("");
            } else {
                userEmail.setHint(R.string.username_hint);
            }
        });

        AtomicReference<String> studentID = new AtomicReference<>(getIntent().getStringExtra("studentID"));
        AtomicReference<String> studentRoll = new AtomicReference<>(getIntent().getStringExtra("studentRoll"));
        AtomicBoolean studentLoggedIn = new AtomicBoolean(getIntent().getBooleanExtra("studentLoggedIn", false));

        Button signInButton = findViewById(R.id.signupbutton01);
        signInButton.setOnClickListener(v -> {
            String userID = userEmail.getText().toString();
            String rollNo = rollNumber.getText().toString();
            if (isValidEmail(userID) && rollNo.length()==8 ) {
                Intent intent = new Intent(StudentActivity.this, StudentSignUp.class);
                intent.putExtra("username", userID);
                intent.putExtra("roll_number", rollNo);
                studentID.set(userID);
                studentRoll.set(rollNo);
                intent.putExtra("studentID", studentID.get());
                intent.putExtra("studentRoll", studentRoll.get());
                intent.putExtra("studentLoggedIn", studentLoggedIn);
                startActivity(intent);
            } else {
                Toast.makeText(StudentActivity.this, "Incorrect Credentials : " + rollNumber.getText().toString().length(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(String email) {
        if (email.equals("Pankaj")) return true;
        return email.endsWith("@nitj.ac.in");
    }
};