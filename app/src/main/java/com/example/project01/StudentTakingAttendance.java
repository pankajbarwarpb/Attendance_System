package com.example.project01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
public class StudentTakingAttendance extends AppCompatActivity {
    private File internalStorageDir;
    private static final String TAG = "FACE_DETECT_TAG";
    private final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private boolean imageCaptured = false;
    private FaceDetector detector;
    Button markButton;
    TextView result_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_taking_attendance);



        String courseID = getIntent().getStringExtra("course");
        String rollNo = getIntent().getStringExtra("rollno");

        Button captureImage = findViewById(R.id.captureImage);
        TextView course = findViewById(R.id.coursename4);
        course.setText(courseID);
        TextView date = findViewById(R.id.currentDate2);

        // set the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());
        date.setText(currentDate);

        File internalStorageDir = getFilesDir();

        File[] files = internalStorageDir.listFiles();

        for (File file : files) {
            if (file.getName().endsWith(".jpg")) {
                // process the image file here
                Log.d(TAG, "Image found");
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ImageView imageView1 = findViewById(R.id.demoImage1);
                imageView1.setImageBitmap(bitmap);
            }
        }

        // FACE DETECTOR OPTIONS
        FaceDetectorOptions realTimeFdo =
                new FaceDetectorOptions.Builder()
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .build();

        // init faceDetector object
        detector = FaceDetection.getClient(realTimeFdo);
    }

    public void takeImage(View view) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, REQUEST_CODE_IMAGE_CAPTURE);
        Toast.makeText(this, "attendance marked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_IMAGE_CAPTURE){
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            ImageView imageView2 = findViewById(R.id.demoImage2);
            imageView2.setImageBitmap(imageBitmap);
            imageCaptured = true;
        }
    }
}