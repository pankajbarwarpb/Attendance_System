package com.example.project01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import java.time.LocalDate;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class StartTakingAttendance extends AppCompatActivity {

    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_taking_attendance);

        Button stopTakingAttendanceButton = findViewById(R.id.stopTakingAttendance);

        stopTakingAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView courseName = findViewById(R.id.coursename3);
        courseID = getIntent().getStringExtra("courseId");

        String shivId = "pankaj_pankaj_CSE_2020_2022";
        courseID = shivId;
        FirebaseDatabase.getInstance().getReference().child("courses").child(shivId).child("details").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseName.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        courseName.setText(courseID);


// Get the current date
        TableLayout table = findViewById(R.id.tablelayout);

        TextView rollNoTextView = new TextView(this);
        TableRow tableRow = new TableRow(this);
        rollNoTextView.setText("Roll No");
        rollNoTextView.setPadding(16, 16, 16, 16);
        rollNoTextView.setTextSize(20);
        tableRow.addView(rollNoTextView);

        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        String dayInMonth = "";
        if (currentDate != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int day = currentDate.getDayOfMonth();
                int month = currentDate.getMonthValue();
                int year = currentDate.getYear();
                dayInMonth = String.format("%d-%02d-%d", day, month, year);
            }
        }

        TextView dayOfMonthTextView = new TextView(this);
        dayOfMonthTextView.setText(dayInMonth);
        dayOfMonthTextView.setPadding(16, 16, 16, 16);
        dayOfMonthTextView.setTextSize(20);
        tableRow.addView(dayOfMonthTextView);
        table.addView(tableRow);


        {
            DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("courses").child(courseID).child("students");
            int month = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                month = currentDate.getMonthValue();
            }
            int year = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                year = currentDate.getYear();
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int day = currentDate.getDayOfMonth();
            }

            int finalYear = year;
            String finalDayInMonth = dayInMonth;
            int finalMonth = month;
            studentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot rollnumber : snapshot.getChildren()) {
                        TableRow newtableRow = new TableRow(StartTakingAttendance.this);
                        TextView textView = new TextView(StartTakingAttendance.this);
                        textView.setText(rollnumber.getKey());
                        newtableRow.addView(textView);

                        FirebaseDatabase.getInstance().getReference().child("courses").child(shivId).child("students").child(rollnumber.getKey()).child(String.valueOf(finalYear)).child(String.valueOf(finalMonth)).child(String.valueOf(finalDayInMonth)).setValue("P");
                        TextView presence = new TextView(StartTakingAttendance.this);
                        Random random = new Random();
                        if (random.nextInt(10) < 8) { // 80% of the time
                            presence.setText("P");
                        } else { // 20% of the time
                            presence.setText("A");
                        }
                        newtableRow.addView(presence);
                        table.addView(newtableRow);

                        newtableRow.setClickable(true);
                        newtableRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView textView = (TextView) newtableRow.getChildAt(0);
                                String name = textView.getText().toString();
                                Intent intent = new Intent(StartTakingAttendance.this, StudentDetails.class);
                                intent.putExtra("courseId", courseID);
                                intent.putExtra("rollnumber", textView.getText().toString());
                                startActivity(intent);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
};
