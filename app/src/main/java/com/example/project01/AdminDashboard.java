package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdminDashboard extends AppCompatActivity {

    Button logOut, addFaculty, addStudent, addCoursesForStudents, addCoursesForFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        logOut = findViewById(R.id.adminLogout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, MainActivity.class);
                Toast.makeText(AdminDashboard.this, "Admin Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        addFaculty = findViewById(R.id.addFacultyDetails);
        addStudent = findViewById(R.id.addStudentDetails);
        addCoursesForFaculty = findViewById(R.id.addCoursesForFaculty);
        addCoursesForStudents = findViewById(R.id.addCoursesForStudents);

        addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, AddFacultyDetails.class);
                startActivity(intent);
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, AddStudentDetails.class);
                startActivity(intent);
            }
        });

        addCoursesForFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, AddCoursesForFaculty.class);
                startActivity(intent);
            }
        });

        addCoursesForStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, AddCoursesForStudents.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}