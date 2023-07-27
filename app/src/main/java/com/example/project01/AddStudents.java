package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddStudents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);

        String courseID = getIntent().getStringExtra("course");
        TextView courseView = findViewById(R.id.coursename2);
        courseView.setText(courseID);

        Button addStudentButton = findViewById(R.id.addStudentButton);
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the desired action when the button is clicked
                // For example, open a new activity or add a new student to the database
                Toast.makeText(AddStudents.this, "Student(s) Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}