package com.example.jerry.FarmStrong;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        renderPoints();

    }

    public void renderPoints()
    {
        File path = this.getFilesDir();
        File file = new File(path,"pins.txt");
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
                String name = in.readLine();
                String x = in.readLine();
                String y = in.readLine();
                Log.e("File Read", name + " " + x + " " + y);
                ImageView point = new ImageView(getApplicationContext());
                point.setTag(name);
                point.setOnClickListener(new View.OnClickListener(){
                    public void onClick (View view)
                    {
                        detailView(view);
                    }
                });
                lp.setMargins(Integer.parseInt(x), Integer.parseInt(y), 0, 0);
                point.setLayoutParams(lp);
                point.getLayoutParams().height = 70;
                point.getLayoutParams().width = 40;
                point.setImageResource(R.drawable.pin);
                rl.addView(point);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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
    public void detailView(View view){
        Intent intent = new Intent(this, DetailView.class);
        intent.putExtra("name",view.getTag().toString());
        startActivity(intent);
    }
}
