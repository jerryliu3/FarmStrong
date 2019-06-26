package com.example.jerry.FarmStrong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Interpolator;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
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
    float xOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        markers = new ArrayList<ImageView>();
        renderPoints();
        FrameLayout frame = (FrameLayout) findViewById(R.id.full_farm);
        PlayAreaView image = new PlayAreaView(this);
        frame.addView(image);
    }

    private class GestureListener implements GestureDetector.OnGestureListener {
        PlayAreaView view;
        public GestureListener(PlayAreaView view) {
            this.view = view;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
        @Override
        public void onShowPress(MotionEvent e) {
        }
        @Override
        public void onLongPress(MotionEvent e) {
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            view.onMove(-distanceX, 0);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               final float velocityX, final float velocityY) {
            final float distanceTimeFactor = 0.4f;
            final float totalDx = (distanceTimeFactor * velocityX/2);
            final float totalDy = (distanceTimeFactor * velocityY/2);
            return true;
        }
    }
    private class PlayAreaView extends View {
        private GestureDetector gestures;
        public PlayAreaView(Context context) {
            super(context);
            translate = new Matrix();
            gestures = new GestureDetector(FarmActivity.this,
                    new GestureListener(this));
            droid = BitmapFactory.decodeResource(getResources(),
                    R.drawable.fullmap);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return gestures.onTouchEvent(event);
        }
        private Matrix translate;
        private Bitmap droid;
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(droid, translate, null);
            Matrix m = canvas.getMatrix();
        }
        public void onMove(float dx, float dy) {
            if(xOffset + dx < -1100 || xOffset + dx > 0){
                dx = 0;
                dy = 0;
            }
            xOffset += dx;
            translate.postTranslate(dx, dy);

            int size = markers.size();
            for(int i=0;i<size;i++){
                ImageView current = markers.get(i);
                int x = current.getLeft(); // x-coordinate
                int y = current.getTop(); // y-coordinate

                current.setLeft(Math.round(x + dx));
                current.setTop(Math.round(y+ dy));
            }

            invalidate();
        }
        public void onResetLocation() {
            translate.reset();
            invalidate();
        }

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
                lp.setMargins(Integer.parseInt(x)+Math.round(xOffset), Integer.parseInt(y), 0, 0);
                point.setContentDescription(description);
                point.setLayoutParams(lp);
                point.getLayoutParams().height = 70;
                point.getLayoutParams().width = 40;
                point.setImageResource(R.drawable.pin);
                rl.addView(point);
                ((ViewGroup)point.getParent()).setClipChildren(false);
                markers.add(point);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch(Exception d){
            d.printStackTrace();
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
