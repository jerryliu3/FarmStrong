package com.example.jerry.FarmStrong;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    private static final long CONNECTION_TIME_OUT_MS = 100;

    private MobileServiceClient azureClient;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*try {
            azureClient = new MobileServiceClient(
                    "https://www.google.com",       // Replace with the Site URL
                    this);                  // Your application Context
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }
    public void dashboardActivity(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
    // Check if external storage is available to read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));

    }

}
