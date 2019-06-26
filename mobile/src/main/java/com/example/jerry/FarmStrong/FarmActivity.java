package com.example.jerry.FarmStrong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Interpolator;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
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
            translate.postTranslate(dx, dy);
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
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = in.readLine()) != null) {
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                String name = line;
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
                point.setLayoutParams(lp);
                point.getLayoutParams().height = 70;
                point.getLayoutParams().width = 40;
                point.setImageResource(R.drawable.pin);
                rl.addView(point);
            }
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
