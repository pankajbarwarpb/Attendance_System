package com.example.project01;

import static com.example.project01.R.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.project01.R.layout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import android.Manifest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

public class StudentSignUp extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    BluetoothAdapter bluetoothAdapter;
    ListView lv;
    ArrayList<String> arrayList;

    private static final int REQUEST_CODE_VIDEO_CAPTURE = 2342;
    private static final long MAX_RECORDING_TIME = 5000; // 5 seconds in milliseconds
    private static final String TAG = "FACE_DETECT_TAG";
    private static final int SCALING_FACTOR = 1;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private File internalStorageDir;
    private ImageView imageView;
    private FaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_student_sign_up);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lv = findViewById(id.deviceList);
        if (bluetoothAdapter == null) {
            Toast.makeText(StudentSignUp.this, "Bluetooth not Supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        assert bluetoothAdapter != null;

        String userID = getIntent().getStringExtra("username");
        String rollNumber = getIntent().getStringExtra("roll_number");
        TextView userNameTextView = findViewById(id.userName);
        TextView rollNumberView = findViewById(id.rollNumber);
        userNameTextView.setText(userID);
        rollNumberView.setText(rollNumber);
//
        AtomicReference<String> studentID = new AtomicReference<>(getIntent().getStringExtra("studentID"));
        AtomicReference<String> studentRoll = new AtomicReference<>(getIntent().getStringExtra("studentRoll"));
        AtomicBoolean studentLoggedIn = new AtomicBoolean(getIntent().getBooleanExtra("studentLoggedIn", false));

        Button scanDevices = findViewById(id.scanDevices);
        scanDevices.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                // ENABLE BLUETOOTH
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(StudentSignUp.this, "Bluetooth Disabled", Toast.LENGTH_SHORT).show();
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(StudentSignUp.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            requestBluetoothConnectPermission();
                        }
                        return;
                    }
//            startActivityForResult(enableBtIntent, 1);
                } else {
                    Toast.makeText(StudentSignUp.this, "Scanning...", Toast.LENGTH_SHORT).show();
                    Set<BluetoothDevice> pairedDevices;
                    pairedDevices = bluetoothAdapter.getBondedDevices();
                    arrayList = new ArrayList<>();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        startDiscovery();
                    }
                }
            }
        });

        // SETTING SCANNER
        mediaMetadataRetriever = new MediaMetadataRetriever();
//        imageView = findViewById(R.id.imageView);
        internalStorageDir = getFilesDir();

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

        Button scanFace = findViewById(id.scanFaces);
        scanFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentSignUp.this, signInFaceDetection.class);
                intent.putExtra("username", userID);
                intent.putExtra("rollnumber", rollNumber);
                intent.putExtra("studentID", studentID);
                intent.putExtra("studentRoll", studentRoll);
                intent.putExtra("studentLoggedIn", studentLoggedIn);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    protected void startDiscovery() {
        if (ContextCompat.checkSelfPermission(StudentSignUp.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        if (ContextCompat.checkSelfPermission(StudentSignUp.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
        }
        if (ContextCompat.checkSelfPermission(StudentSignUp.this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 4);
        }
        if (ContextCompat.checkSelfPermission(StudentSignUp.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 4);
        }

        bluetoothAdapter.startDiscovery();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Bluetooth", "Found");
                String action = intent.getAction();
                if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (ActivityCompat.checkSelfPermission(StudentSignUp.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    String deviceDetails = device.getName()+" " +device.getAddress();
                    Log.i("BlueTooth_Devices", deviceDetails);
                    boolean found = false;
                    for(String str : arrayList){
                        if(str.equals(deviceDetails)) {
                            found = true;
                            break;
                        }
                    }
                    if(!found)
                        arrayList.add(deviceDetails);
                }
                if(arrayList.size()!=0){
                    ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                    lv.setAdapter(itemAdapter);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private static final int REQUEST_BLUETOOTH_CONNECT = 1;

    // Call this method to request the BLUETOOTH_CONNECT permission
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestBluetoothConnectPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                REQUEST_BLUETOOTH_CONNECT);
    }

    // Override this method to handle the user's response to the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with Bluetooth discovery
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}