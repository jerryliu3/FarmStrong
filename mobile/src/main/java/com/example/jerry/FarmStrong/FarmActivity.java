package com.example.jerry.FarmStrong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FarmActivity extends AppCompatActivity {

    ArrayList<ImageView> markers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        markers = new ArrayList<ImageView>();
        renderPoints();



    }

    public void renderPoints()
    {
        File path = this.getFilesDir();
        File file = new File(path,"pins.txt");
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.constraint);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        try {

            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = in.readLine()) != null) {
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                String name = line;
                String description = in.readLine();
                String x = in.readLine();
                String y = in.readLine();
                Log.e("File Read", name + " " + x + " " + y);
                ImageView point = new ImageView(getApplicationContext());
                point.setTag(name);
                point.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        detailView(view);
                    }
                });
                lp.setMargins(Integer.parseInt(x), Integer.parseInt(y), 0, 0);
                point.setContentDescription(description);
                point.setLayoutParams(lp);
                point.getLayoutParams().height = 70;
                point.getLayoutParams().width = 40;
                point.setImageResource(R.drawable.pin);
                rl.addView(point);
                markers.add(point);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        int size = markers.size();
        for(int i = 0; i < size;i++){
            ((ViewGroup)markers.get(0).getParent()).removeView(markers.get(0));
            markers.remove(0);
        }
        renderPoints();
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
    public void clearAll(View view) {
        File path = this.getFilesDir();
        File file = new File(path,"pins.txt");
        FileOutputStream fos = null;
        try{
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        onResume();
    }
    public void detailView(View view){
        Intent intent = new Intent(this, DetailView.class);
        intent.putExtra("name",view.getTag().toString());
        intent.putExtra("classification",view.getContentDescription().toString());
        Log.d("Intent Input",view.getTag().toString() + " " + view.getContentDescription().toString());
        startActivity(intent);
    }
}
