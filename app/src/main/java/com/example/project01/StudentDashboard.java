package com.example.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

class CourseEnrolled implements Serializable {
    public String branch, year, credit, name, batch, code;
    public boolean sett;
    public void setValues(String a, String b, String c, String d, String e, String f){
        this.name = a;
        this.code = b;
        this.credit = c;
        this.branch = d;
        this.batch = e;
        this.year = f;
        this.sett = true;
    }
};

public class StudentDashboard extends AppCompatActivity {

    private LinearLayout coursesContainer;
    private ListView coursesListView;

//    Course[] courses = new Course[10];

    private Button cancelAddButton;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        coursesContainer = (LinearLayout) findViewById(R.id.coursesContainer);


        AtomicReference<String> studentID = new AtomicReference<>(getIntent().getStringExtra("studentID"));
        AtomicReference<String> studentRoll = new AtomicReference<>(getIntent().getStringExtra("studentRoll"));
        AtomicBoolean studentLoggedIn = new AtomicBoolean(getIntent().getBooleanExtra("studentLoggedIn", false));

        String userID = String.valueOf(studentID);
        String rollNo = String.valueOf(studentRoll);
        TextView userTextView = findViewById(R.id.studentID);

        userTextView.setText(rollNo);

//        for (int i = 0; i < courses.length; i++) {
//            courses[i] = new Course();
//            courses[i].sett = false;
//        }

//        courses[0].setValues("Operating System", "CSPE-301", "3", "CSE", "2020", "2022");
//        for (int i = 0; i < coursesContainer.getChildCount(); i++) {
//            coursesContainer.getChildAt(i).setVisibility(View.VISIBLE);
//        }

        coursesListView = (ListView) findViewById(R.id.coursesListView);
        coursesListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));

        Button logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDashboard.this, MainActivity.class);
                studentLoggedIn.set(false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
//                courses = (Course[]) data.getSerializableExtra("courses");
                updateCoursesDisplay();
            }
        }
    }


    @Override
    protected void onResume () {
        super.onResume();
        updateCoursesDisplay();
    }

    private void updateCoursesDisplay () {
        coursesContainer.removeAllViews(); // remove all previous views
        String setin = "";
        for (int i = 0; i < 10; i++) {
//            Toast.makeText(TeacherDashboard.this, "Set in : " + String.valueOf(i), Toast.LENGTH_SHORT).show();
//            if (courses[i].sett) {
//                setin += (String.valueOf(i) + " ");
//                Button button = new Button(StudentDashboard.this);
//                String courseDetails = courses[i].name + " (" + courses[i].branch + "-" + courses[i].batch + ")";
//                button.setText(courseDetails);
//                button.setTextSize(16);
//                button.setTextColor(Color.parseColor("#000000"));

//                button.setBackgroundColor(Color.parseColor("#e3b4ed"));
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//                int margin = getResources().getDimensionPixelSize(R.dimen.text_view_margin);
//                params.setMargins(margin, margin, margin, margin);
//                button.setLayoutParams(params);
//                int paddingLeft = getResources().getDimensionPixelSize(R.dimen.text_view_padding_left);
//                button.setPadding(paddingLeft, 0, 0, 0);
//                button.setHeight(100);

// Create a shape drawable with rounded corners
                GradientDrawable shapeDrawable = new GradientDrawable();
                shapeDrawable.setShape(GradientDrawable.RECTANGLE);
                shapeDrawable.setCornerRadii(new float[]{50, 50, 50, 50, 50, 50, 50, 50}); // adjust the radii to your liking
                shapeDrawable.setColor(Color.parseColor("#e3b4ed"));

// Set the shape drawable as the background of the Button
//                button.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//                button.setBackground(shapeDrawable);

                // Add a long click listener to the button
                int finalI = i;
//                button.setOnLongClickListener(new View.OnLongClickListener() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public boolean onLongClick(View v) {
//                        // Create a custom Toast to show the course details
//                        Course course = courses[finalI];
//                        LayoutInflater inflater = getLayoutInflater();
//                        View toastLayout = inflater.inflate(R.layout.toast_layout, (LinearLayout) findViewById(R.id.toast_root));
//                        TextView courseNameTextView = (TextView) toastLayout.findViewById(R.id.courseNameTextView);
//                        courseNameTextView.setId(R.id.courseNameTextView);
//                        courseNameTextView.setText("Name: " + course.name);
//
//                        TextView courseCodeTextView = (TextView) toastLayout.findViewById(R.id.courseCodeTextView);
//                        courseCodeTextView.setId(R.id.courseCodeTextView);
//                        courseCodeTextView.setText("Code: " + course.code);
//
//                        TextView courseCreditTextView = (TextView) toastLayout.findViewById(R.id.courseCreditTextView);
//                        courseCreditTextView.setId(R.id.courseCreditTextView);
//                        courseCreditTextView.setText("Credit: " + course.credit);
//
//                        TextView courseBranchTextView = (TextView) toastLayout.findViewById(R.id.courseBranchTextView);
//                        courseBranchTextView.setId(R.id.courseBranchTextView);
//                        courseBranchTextView.setText("Branch: " + course.branch);
//
//                        TextView courseBatchTextView = (TextView) toastLayout.findViewById(R.id.courseBatchTextView);
//                        courseBatchTextView.setId(R.id.courseBatchTextView);
//                        courseBatchTextView.setText("Batch: " + course.batch);
//
//                        TextView courseYearTextView = (TextView) toastLayout.findViewById(R.id.courseYearTextView);
//                        courseYearTextView.setId(R.id.courseYearTextView);
//                        courseYearTextView.setText("Year: " + course.year);
//
//                        Toast toast = new Toast(StudentDashboard.this);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.setDuration(Toast.LENGTH_LONG);
//                        toast.setView(toastLayout);
//                        toast.show();
//                        // Cancel the Toast after 2 seconds
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                toast.cancel();
//                            }
//                        }, 2000);
//                        return true;
//                    }
//                });

                String userID = getIntent().getStringExtra("username");

//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(StudentDashboard.this, StudentDetails.class);
//                        intent.putExtra("course", courses[finalI].name);
//                        intent.putExtra("username", userID);
//                        startActivity(intent);
//                    }
//                });
//
//                coursesContainer.addView(button);
            }
        }
//    }

    @Override
    public void onBackPressed() {
        // Do nothing or handle the back button press as needed
    }
};