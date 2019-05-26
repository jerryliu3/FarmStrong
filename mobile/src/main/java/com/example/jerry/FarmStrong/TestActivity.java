package com.example.jerry.FarmStrong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private static final long CONNECTION_TIME_OUT_MS = 100;

    private GoogleApiClient client;
    private MobileServiceClient azureClient;
    private String nodeId;
    private static final String TAG = "MainActivity";
    private ArrayList<String> readings;
    TextView main;
    Button exit;
    Switch toggleIntent;
    boolean intentOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (TextView) findViewById(R.id.helloworld);
        readings = new ArrayList<String>();
        exit = (Button) findViewById(R.id.exit);
        /*try {
            azureClient = new MobileServiceClient(
                    "https://www.google.com",       // Replace with the Site URL
                    this);                  // Your application Context
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
  }
}
