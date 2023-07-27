package com.example.project01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;

public class CourseDetails extends AppCompatActivity {
    private String courseId;
    private int daysInMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        TextView courseName = findViewById(R.id.coursename);
        courseId = getIntent().getStringExtra("courseId");

        String shivId = "pankaj_pankaj_CSE_2020_2022";
        courseId = shivId;
        FirebaseDatabase.getInstance().getReference().child("courses").child(shivId).child("details").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseName.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TableLayout tableLayout = findViewById(R.id.tablelayout);

        Spinner monthSpinner = findViewById(R.id.month_spinner);
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); // Note: January is 0
        monthSpinner.setSelection(currentMonth);

        Spinner yearSpinner = findViewById(R.id.year_spinner);
        int currentYear = calendar.get(Calendar.YEAR);
        ArrayAdapter<String> yearAdapter = (ArrayAdapter<String>) yearSpinner.getAdapter();
        int yearPosition = yearAdapter.getPosition(String.valueOf(currentYear));
        yearSpinner.setSelection(6);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Do something when a new item is selected
                int selectedMonth = monthSpinner.getSelectedItemPosition() + 1;
                Toast.makeText(CourseDetails.this, "month : " + selectedMonth, Toast.LENGTH_SHORT).show();
                updateTable();
                Log.d("TAG", "Selected month: " + selectedMonth);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });



        TextView rollNoTextView = new TextView(this);
        TableRow tableRow = new TableRow(this);
        rollNoTextView.setText("Roll No");
        rollNoTextView.setPadding(16, 16, 16, 16);
        tableRow.addView(rollNoTextView);
        daysInMonth = getDaysInMonth(monthSpinner.getSelectedItemPosition());

        // Calculate the width of each column
        int columnWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

        updateTable();
    }

    private void updateTable() {
        TableLayout tableLayout = findViewById(R.id.tablelayout);
        tableLayout.removeAllViews();

        Spinner monthSpinner = findViewById(R.id.month_spinner);
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); // Note: January is 0

        Spinner yearSpinner = findViewById(R.id.year_spinner);
        int currentYear = calendar.get(Calendar.YEAR);
        ArrayAdapter<String> yearAdapter = (ArrayAdapter<String>) yearSpinner.getAdapter();
        int yearPosition = yearAdapter.getPosition(String.valueOf(currentYear));
        yearSpinner.setSelection(6);


        TextView rollNoTextView = new TextView(this);
        TableRow tableRow = new TableRow(this);
        rollNoTextView.setText("Roll No");
        rollNoTextView.setPadding(16, 16, 16, 16);
        tableRow.addView(rollNoTextView);
        daysInMonth = getDaysInMonth(monthSpinner.getSelectedItemPosition());

        // Calculate the width of each column
        int columnWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

// Add day numbers from the start of the month to the end of the month to other columns
        for (int i = 1; i <= daysInMonth; i++) {
            TextView dayTextView = new TextView(this);
            dayTextView.setText(String.valueOf(i));
            dayTextView.setPadding(16, 16, 16, 16);

            // Set the layout parameters for the TextView
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 10, 0); // Set margin of 10 pixels to the right
            dayTextView.setLayoutParams(params);
            tableRow.addView(dayTextView);
        }

        tableRow.setBackgroundColor(Color.rgb(200, 72, 72));
        tableLayout.addView(tableRow);

        String shivId = "pankaj_pankaj_CSE_2020_2022";
        courseId = shivId;
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("courses").child(courseId).child("students");
        FirebaseDatabase.getInstance().getReference().child("courses").child(shivId).child("students").child("20103101").child("2023").child("5").child("12").setValue("P");
        int month = monthSpinner.getSelectedItemPosition() + 1;
        int year = currentYear;

        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot rollnumber : snapshot.getChildren()) {
                    TableRow newtableRow = new TableRow(CourseDetails.this);
                    TextView textView = new TextView(CourseDetails.this);
                    textView.setText(rollnumber.getKey());
                    textView.setPadding(16, 16, 16, 16);
                    newtableRow.addView(textView);
                    for (DataSnapshot fyear : rollnumber.getChildren()) {
                        if (!fyear.getKey().equals(String.valueOf(year))) continue;
                        for (DataSnapshot fmonth : fyear.getChildren()) {
                            if (!fmonth.getKey().equals(String.valueOf(month))) continue;
                            int day = 1;
                            for (DataSnapshot fday : fmonth.getChildren()) {
                                while (day != Integer.parseInt(fday.getKey())) {
                                    TextView newView = new TextView(CourseDetails.this);
                                    newView.setText(" ");
                                    newView.setPadding(16, 16, 16, 16);
                                    newtableRow.addView(newView);
                                    day++;
                                }
                                TextView newView = new TextView(CourseDetails.this);
                                newView.setText(fday.getValue(String.class));
                                newView.setPadding(16, 16, 16, 16);
                                newtableRow.addView(newView);
                            }
                            while (day <= daysInMonth) {
                                TextView newView = new TextView(CourseDetails.this);
                                newView.setText(" ");
                                newView.setPadding(16, 16, 16, 16);
                                newtableRow.addView(newView);
                                day++;
                            }
                        }
                    }
                    tableLayout.addView(newtableRow);
                    newtableRow.setClickable(true);
                    newtableRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView textView = (TextView) newtableRow.getChildAt(0);
                            String name = textView.getText().toString();
                            Intent intent = new Intent(CourseDetails.this, StudentDetails.class);
                            intent.putExtra("courseId", courseId);
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

        TableRow firstRow = (TableRow) tableLayout.getChildAt(0); // Get the first row
        int rows = tableLayout.getChildCount();
        int cols = ((TableRow) tableLayout.getChildAt(0)).getChildCount();

        TableRow totaltableRow = new TableRow(this);
        TextView totalTextView = new TextView(this);
        totalTextView.setText("Total");
        totalTextView.setPadding(16, 16, 16, 16);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 10, 0); // Set margin of 10 pixels to the right
        totalTextView.setLayoutParams(params);

        totaltableRow.addView(totalTextView);

        for (int i = 1; i <= daysInMonth; i++) {
            TextView dayTextView = new TextView(this);
            dayTextView.setPadding(2, 2, 2, 2);

            // Set the layout parameters for the TextView
            TableRow.LayoutParams _params = new TableRow.LayoutParams(columnWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            dayTextView.setLayoutParams(_params);

            int pCount = 0; // initialize the count variable to 0

// loop through each column in the table except the first column (which contains the roll no)
            for (int j = 1; j < tableLayout.getChildCount(); j++) {
                TextView tt = (TextView) ((TableRow) tableLayout.getChildAt(j)).getChildAt(i);
                if(tt==null) continue;
                String text = tt.getText().toString(); // Get the text of the TextView
                if (text.equals("P")) {
                    pCount++; // increment the count if the text is "P"
                }
            }

            dayTextView.setText(String.valueOf(pCount));
            if(pCount==0) dayTextView.setText("");
            dayTextView.setBackgroundResource(R.drawable.total_border);

            totaltableRow.addView(dayTextView);
        }

        TextView textView = (TextView) firstRow.getChildAt(0); // Get the first column of the first row

        totaltableRow.setBackgroundColor(Color.rgb(153, 178, 222));
        totaltableRow.setGravity(Gravity.CENTER);
        tableLayout.addView(totaltableRow);

        rows = tableLayout.getChildCount();
        cols = ((TableRow) tableLayout.getChildAt(0)).getChildCount();


        Button takeAttendanceButton = findViewById(R.id.takeAttendance);
        takeAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartTakingAttendance.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });
    }

    private int getDaysInMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}