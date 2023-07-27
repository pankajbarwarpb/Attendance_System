package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class AddCoursesForStudents extends AppCompatActivity {

    private EditText FirstRange;
    private EditText SecondRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses_for_students);

        FirstRange = findViewById(R.id.startRange);
        SecondRange = findViewById(R.id.endRange);
        EditText Code = findViewById(R.id.stCourseCode);
        EditText Branch = findViewById(R.id.stCourseBranch);
        EditText Name = findViewById(R.id.stCourseName);
        EditText Batch = findViewById(R.id.stCourseBatch);
        EditText Year = findViewById(R.id.stCourseYear);
        EditText Id = findViewById(R.id.stFacultyID);
        EditText Credit = findViewById(R.id.stCourseCredit);

        Button addButton = findViewById(R.id.addCourseForStudent);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstRange = FirstRange.getText().toString().trim();
                String secondRange = SecondRange.getText().toString().trim();
                String code = Code.getText().toString().trim();
                String name = Name.getText().toString().trim();
                String branch = Branch.getText().toString().trim();
                String batch = Batch.getText().toString().trim();
                String year = Year.getText().toString().trim();
                String id = Id.getText().toString().trim();
                String credit = Credit.getText().toString().trim();

                String courseID = id+"_"+code+"_"+branch+"_"+batch+"_"+year;

                if(secondRange == null || secondRange == ""){
                    secondRange = firstRange;
                }
                Toast toast = Toast.makeText(AddCoursesForStudents.this, "", Toast.LENGTH_SHORT);
                for(int i = Integer.parseInt(String.valueOf(firstRange)); i<= Integer.parseInt(String.valueOf(secondRange)); i++){
                    FirebaseDatabase.getInstance().getReference().child("courses").child(courseID).child("students").child(String.valueOf(i)).child("added").setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("studentDetails").child(String.valueOf(i)).child("courses").child(courseID).setValue(true);
                    if(toast!=null){
                        toast.cancel();
                    }
                    assert toast != null;
                    toast.setText("added : " + i);
                    toast.show();
                }
                Toast.makeText(AddCoursesForStudents.this, "Added = " + firstRange + " : " + secondRange, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}