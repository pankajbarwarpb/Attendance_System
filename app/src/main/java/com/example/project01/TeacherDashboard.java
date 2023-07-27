package com.example.project01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeacherDashboard extends AppCompatActivity {
    private LinearLayout coursesContainer;
    private ListView coursesListView;
    private DatabaseReference reference, courseRef;
    private Set<String> set;
    FrameLayout coursesListViewContainer;
    String teacherID;
    Set<Map.Entry<String, HashMap<String, String>>> mSet;
    Map.Entry<String, HashMap<String, String>> mappingString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        Button logout = findViewById(R.id.tLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        coursesContainer = (LinearLayout) findViewById(R.id.coursesContainer);
        teacherID = getIntent().getStringExtra("username");

        TextView teacherName = findViewById(R.id.teacherName);
        teacherName.setText(teacherID);

        coursesListViewContainer = findViewById(R.id.coursesListViewContainer);
        set = new HashSet<>();
        coursesListView = (ListView) findViewById(R.id.coursesListView);
        coursesListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));

        mSet = new HashSet<>();

        refreshCourses();

        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCourses();
//                Toast.makeText(TeacherDashboard.this, "Total courses : " + set.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (set.isEmpty()) {
            coursesListViewContainer.setVisibility(View.GONE);
        } else {
            coursesListViewContainer.setVisibility(View.GONE);
        }
    }

    private void refreshCourses(){
        // Get reference to the LinearLayout
        LinearLayout coursesContainer = findViewById(R.id.coursesContainer);

        int[] size = new int[1];
        reference = FirebaseDatabase.getInstance().getReference().child("facultyDetails").child(teacherID).child("courses");
        Toast.makeText(TeacherDashboard.this, "Refresh", Toast.LENGTH_SHORT).show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String id = dataSnapshot.getKey();
                        if(set.contains(id)) continue;
                        set.add(id);
                        HashMap<String , Object> hashMap = new HashMap<>();
                        courseRef = FirebaseDatabase.getInstance().getReference().child("courses").child(id).child("details");

                        courseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
//                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        String name = snapshot.child("name").getValue(String.class);
                                        String branch = snapshot.child("branch").getValue(String.class);
                                        String batch = snapshot.child("batch").getValue(String.class);
                                        String year = snapshot.child("year").getValue(String.class);
                                        // do something with the string values here
                                        makeButton(name+" "+branch+"_"+batch+"_"+year, id);
//                                        Toast.makeText(TeacherDashboard.this, name, Toast.LENGTH_SHORT).show();
//                                    }
                                }catch (Exception e) {
                                    e.printStackTrace();
//                                    Toast.makeText(TeacherDashboard.this, "childError: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(TeacherDashboard.this, "coursesError: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            private void makeButton(String course, String courseID){
//                Toast.makeText(TeacherDashboard.this, "found : " + course, Toast.LENGTH_SHORT).show();
                Button courseButton = new Button(TeacherDashboard.this);

                // Set the text of the Button to the course name
                courseButton.setText(course);
                GradientDrawable shapeDrawable = new GradientDrawable();
                shapeDrawable.setShape(GradientDrawable.RECTANGLE);
                shapeDrawable.setCornerRadius(50); // adjust the radii to your liking
                shapeDrawable.setColor(Color.parseColor("#e3b4ed"));
                courseButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                courseButton.setBackground(shapeDrawable);
                courseButton.setTextSize(16);
                courseButton.setTextColor(Color.parseColor("#000000"));
                courseButton.setBackgroundColor(Color.parseColor("#e3b4ed"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                int margin = getResources().getDimensionPixelSize(R.dimen.text_view_margin);
                params.setMargins(margin, margin, margin, margin);
                courseButton.setLayoutParams(params);
                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.text_view_padding_left);
                courseButton.setPadding(paddingLeft, 0, 0, 0);
                courseButton.setHeight(100);

                // Set the onClickListener for the Button
                courseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle button click event
                        Intent intent = new Intent(TeacherDashboard.this, CourseDetails.class);
                        intent.putExtra("courseId", courseID);
                        startActivity(intent);
                    }
                });

                // Add the Button to the LinearLayout
                coursesContainer.addView(courseButton);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(TeacherDashboard.this, "Cancelled: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        //
    }
}