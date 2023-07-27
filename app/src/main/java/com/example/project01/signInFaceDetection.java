package com.example.project01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class signInFaceDetection extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 2342;
    private static final String TAG = "FACE_DETECT_TAG";
    private static final int SCALING_FACTOR = 1;
    private File internalStorageDir;
    private ImageView imageView;
    private FaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_face_detection);

        String userID = getIntent().getStringExtra("username");
        String rollNumber = getIntent().getStringExtra("rollnumber");

        imageView = findViewById(R.id.imageView);
        internalStorageDir = getFilesDir();

        // FACE DETECTOR OPTIONS
        FaceDetectorOptions realTimeFdo =
                new FaceDetectorOptions.Builder()
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .build();

        // init faceDetector object
        detector = FaceDetection.getClient(realTimeFdo);


        AtomicReference<String> studentID = new AtomicReference<>(getIntent().getStringExtra("studentID"));
        AtomicReference<String> studentRoll = new AtomicReference<>(getIntent().getStringExtra("studentRoll"));
        AtomicBoolean studentLoggedIn = new AtomicBoolean(getIntent().getBooleanExtra("studentLoggedIn", false));

        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internalStorageDir.isDirectory()) {
                    String[] files = internalStorageDir.list();
                    if (files != null) {
                        int numberOfFiles = files.length;
                        if(numberOfFiles>0){
                            Intent intent = new Intent(signInFaceDetection.this, StudentDashboard.class);
                            intent.putExtra("rollnumber", rollNumber);
                            intent.putExtra("username", userID);

                            studentLoggedIn.set(true);
                            intent.putExtra("studentID", studentID.get());
                            intent.putExtra("studentRoll", studentRoll.get());
                            intent.putExtra("studentLoggedIn", studentLoggedIn);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public void recordVideo(View view) {
        if (!internalStorageDir.exists()) {
            internalStorageDir.mkdirs();
        } else {
            // Get all files in the directory
            File[] files = internalStorageDir.listFiles();
            // Loop through and delete each file
            assert files != null;
            for (File file : files) {
                file.delete();
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if (bitmap != null) {
                // Do something with the bitmap
                String t = "";
                if (internalStorageDir.isDirectory()) {
                    String[] files = internalStorageDir.list();
                    if (files != null) {
                        int numberOfFiles = files.length;
                        // Use numberOfFiles variable as needed
                        t+=String.valueOf(numberOfFiles);
                        t += " ";
                    }
                }
                analyzePhoto(bitmap);
                Toast.makeText(this, t + "Files", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,  "Bitmap error", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ANALYZING THE PHOTO
    private void analyzePhoto(Bitmap bitmap){
        Log.d(TAG, "analyze photo");

        // get smaller bitmap to do analyze process faster
//        Bitmap smallerBitmap = Bitmap.createScaledBitmap(
//                bitmap,
//                bitmap.getWidth()/SCALING_FACTOR,
//                bitmap.getHeight()/SCALING_FACTOR,
//                false
//        );
        Bitmap smallerBitmap = bitmap;

        // get input image using bitmap
        InputImage inputImage = InputImage.fromBitmap(smallerBitmap, 0);

        detector.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        Log.d(TAG, "onSuccess : No of faces detected = " + faces.size());
                        for(Face face: faces) {
                            //Get detected face as rectangle
                            Rect rect = face.getBoundingBox ();
//                            rect.set(rect. left * SCALING_FACTOR,
//                                    0,
//                                    rect.right * SCALING_FACTOR,
//                                    (rect.bottom * SCALING_FACTOR) + 90
//                            );
                        }

                        cropDetectedFaces(bitmap, faces);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure", e);
                        Toast.makeText (signInFaceDetection.this, "Detection failed due to "+e.getMessage (), Toast.LENGTH_SHORT) .show();
                    }
                });
    }

    private void cropDetectedFaces(Bitmap bitmap, List<Face> faces) {
        Log. d(TAG, "cropDetectedFaces: ");
        //Face was detected, Now we will crop the face part of the image
        //there can be multiple faces, you can use loop to manage each, i'm managing the first face from the list List<Face> faces
        Rect rect = faces.get(0) .getBoundingBox ();
        int x = Math.max(rect.left, 0);
        int y = Math. max(rect.top, 0);
        int width = rect.width();
        int height = rect.height();

//        Bitmap croppedBitmap = Bitmap.createBitmap (
//                bitmap,
//                x, y,
//                (x + width > bitmap.getWidth()) ? bitmap.getWidth() - x :  width,
//                (y + height > bitmap.getHeight ()) ? bitmap.getHeight() - y : height
//        );

        Bitmap croppedBitmap = bitmap;

        // set the cropped bitmap to image view
        imageView.setImageBitmap(croppedBitmap);

        String fileName = "frame.jpg";
        File outputFileInternal = new File(internalStorageDir, fileName);
        Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "file name : " + fileName);

        try (FileOutputStream fos = new FileOutputStream(outputFileInternal)) {
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
