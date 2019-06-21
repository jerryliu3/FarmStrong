package com.example.jerry.FarmStrong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

    }
    public void dashboardActivity(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
    public void farmActivity(View view) {
        Intent intent = new Intent(this, FarmActivity.class);
        startActivity(intent);
    }
    public void captureActivity(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
    }
    public void profileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

}
