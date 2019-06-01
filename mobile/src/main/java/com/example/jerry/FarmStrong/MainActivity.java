package com.example.jerry.FarmStrong;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    private static final long CONNECTION_TIME_OUT_MS = 100;

    private MobileServiceClient azureClient;
    private static final String TAG = "MainActivity";
    TextView main;
    Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (TextView) findViewById(R.id.helloworld);
        exit = (Button) findViewById(R.id.exit);
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

    public void classifierActivity(View view) {
        Intent intent = new Intent(this, ClassifierActivity.class);
        startActivity(intent);
    }

    public void captureActivity(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
    }

    // Check if external storage is available to read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));

    }

    public void saveData(View view) {
        Log.i("jerry.FarmStrong", "Saving data");
        try {
            String externalfilename = "HeartRateData.txt";
            if (isExternalStorageWritable()) {
            } else {
                main.setText("not writable");
            }
            File file = Environment.getExternalStorageDirectory();
            File newFile = new File(file, "Wearable App");
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
         /*int variable = 0;
         if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
         {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, variable);
         }*/
            File textFile = new File(newFile, externalfilename);
            FileWriter writer = new FileWriter(textFile, true);
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void exit(View view){
        finish();
    }
}
