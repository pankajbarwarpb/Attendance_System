package com.example.project01;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.DayViewDecorator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudentDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_details);

        Spinner monthSpinner = findViewById(R.id.month_spinner);
        Spinner yearSpinner = findViewById(R.id.year_spinner);
        CalendarView calendarView = findViewById(R.id.calendar);

        String rollNo = getIntent().getStringExtra("rollnumber");
        String CourseID = getIntent().getStringExtra("courseId");

        TextView rollNumber = findViewById(R.id.coursename);
        if(rollNo==null){
            rollNumber.setText(CourseID);
        }
        else
            rollNumber.setText(rollNo);

        if (rollNo != null) {
            updateCalendarFromCSV(rollNo);
        }
        else{
            rollNo = "20103105";
            updateCalendarFromCSV(rollNo);
        }

        Button attendanceMark = findViewById(R.id.markAttendance);

        String cameFrom = getIntent().getStringExtra("teacher");
        if(cameFrom!=null && cameFrom.equals("TEACHER")){
            attendanceMark.setVisibility(View.INVISIBLE);
        }
        String finalRollNo = rollNo;
        attendanceMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentDetails.this, StudentTakingAttendance.class);
                intent.putExtra("course", CourseID);
                intent.putExtra("rollno", finalRollNo);
                startActivity(intent);
            }
        });

        String userID = getIntent().getStringExtra("username");


// get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

// set spinners to current month and year
        monthSpinner.setSelection(currentMonth);
        yearSpinner.setSelection(currentYear - 2017);

// add listeners to spinners to update calendar
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // get current selection in month spinner
                int month = monthSpinner.getSelectedItemPosition();

                // get selected year
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());

                // update calendar view
                calendarView.setDate(getTimeInMillis(year, month));
            }

            private long getTimeInMillis(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, 1);
                return calendar.getTimeInMillis();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });


        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // get current selection in month spinner
                int month = monthSpinner.getSelectedItemPosition();

                // get selected year
                int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());

                // update calendar view
                calendarView.setDate(getTimeInMillis(year, month));
            }

            private long getTimeInMillis(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, 1);
                return calendar.getTimeInMillis();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    private void updateCalendarFromCSV(String rollNo) {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("attedanceCSV.csv");

            // create CSVReader object
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            // read all rows from the CSV file
            List<String[]> rows = reader.readAll();

            // find the row with the given roll number
            for (String[] row : rows) {
                if (row[0].equals(rollNo)) {
                    // update calendar for this row
                    updateCalendarForRow(row);
                    break;
                }
            }

            // close the reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // handle file access error here, e.g. show an error message to the user
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }



    private void updateCalendarForRow(String[] row) {
        // loop over columns and update calendar accordingly

        // set the current day to green
        CalendarView calendarView = findViewById(R.id.calendar);
        Calendar today = Calendar.getInstance();
        calendarView.setDate(today.getTimeInMillis());
        List<DayViewDecorator> decorators = new ArrayList<>();
        decorators.add(new EventDecorator(Color.GREEN, today.getTimeInMillis()));

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int totalCount = 30;
        int pCount = 0;
        for (int i = 1; i <= 30; i++) {

            String value = row[i];
            if (value.equals("P")) {
                // set date to green
                pCount++;
                long timeInMillis = getTimeInMillis(year, month, i);
                decorators.add(new EventDecorator(Color.GREEN, timeInMillis));
            } else if (value.equals("A")) {
                // set date to red
                long timeInMillis = getTimeInMillis(year, month, i);
                decorators.add(new EventDecorator(Color.RED, timeInMillis));
            } else {
                // do nothing
                totalCount--;
            }
        }

        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);

        textView.setText("This Month : " + String.valueOf(pCount) + " / " + String.valueOf(totalCount));
        textView2.setText("Total : " + String.valueOf(pCount) + " / " + String.valueOf(totalCount));

        // add all decorators to calendar view
    }


    private long getTimeInMillis(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }
}