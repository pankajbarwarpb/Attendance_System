package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCoursesForFaculty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses_for_faculty);

        EditText addFacultyId = findViewById(R.id.addFacultyID);
        EditText addCourseName = findViewById(R.id.addCourseName);
        EditText addCourseCode = findViewById(R.id.addCourseCode);
        EditText addBranchName = findViewById(R.id.addBranchName);
        EditText addBatch = findViewById(R.id.addBatch);
        EditText addYear = findViewById(R.id.addYear);
        EditText courseCredit = findViewById(R.id.courseCredit);

        Button addCourse = findViewById(R.id.addCourseForFaculty);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = addFacultyId.getText().toString().trim();
                String name = addCourseName.getText().toString().trim();
                String code = addCourseCode.getText().toString().trim();
                String branch = addBranchName.getText().toString().trim();
                String batch = addBatch.getText().toString().trim();
                String year = addYear.getText().toString().trim();
                String credit = courseCredit.getText().toString().trim();

                String courseID = id+"_"+code+"_"+branch+"_"+batch+"_"+year;

                HashMap<String, Object> map = new HashMap<>();

                map.put("facultyId", id);
                map.put("name", name);
                map.put("code", code);
                map.put("branch", branch);
                map.put("batch", batch);
                map.put("year", year);
                map.put("credit", credit);

                FirebaseDatabase.getInstance().getReference().child("courses").child(courseID).child("details").setValue(map);
                FirebaseDatabase.getInstance().getReference().child("facultyDetails").child(id).child("courses").child(courseID).setValue(courseID);
                Toast.makeText(AddCoursesForFaculty.this, "Course Added" + courseID, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}