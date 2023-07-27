package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kotlin.jvm.internal.MagicApiIntrinsics;

public class MainActivity extends AppCompatActivity {

    public boolean studentLoggedIn;
    public String studentID, studentRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(studentLoggedIn){
            Intent intent = new Intent(MainActivity.this, StudentDashboard.class);
            intent.putExtra("studentID", studentID);
            intent.putExtra("studentRoll", studentRoll);
            intent.putExtra("studentLoggedIn", studentLoggedIn);
            startActivity(intent);
        }

        Button studentButton = findViewById(R.id.student);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                Toast.makeText(MainActivity.this, "Student", Toast.LENGTH_SHORT).show();
                intent.putExtra("studentID", studentID);
                intent.putExtra("studentRoll", studentRoll);
                intent.putExtra("studentLoggedIn", studentLoggedIn);
                startActivity(intent);
            }
        });

        Button teacherButton = findViewById(R.id.teacher);
        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                Toast.makeText(MainActivity.this, "Teacher", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Button adminButton = findViewById(R.id.adminButton);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                Toast.makeText(MainActivity.this, "Admin", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}