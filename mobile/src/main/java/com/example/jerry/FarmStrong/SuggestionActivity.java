package com.example.jerry.FarmStrong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class SuggestionActivity extends AppCompatActivity {
    TextView classText;
    ArrayList<ImageView> markers;

    boolean firstTouch = false;
    ImageView point;
    int xFinal;
    int yFinal;
    float xOffset;
    float yOffset;
    String message;
    Bitmap imageBitmap;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        Intent intent = getIntent();
        imageBitmap = intent.getParcelableExtra("img");
        message = intent.getStringExtra("class");
        markers = new ArrayList<ImageView>();

        ImageView farm = findViewById(R.id.farm);
        point = new ImageView(getApplicationContext());
        //farm.setOnTouchListener(handleTouch);
        renderPoints();

        FrameLayout frame = (FrameLayout) findViewById(R.id.full_farm);
        SuggestionActivity.PlayAreaView image = new SuggestionActivity.PlayAreaView(this);
        frame.addView(image);
        //classText.setText("The class of disease found was: " + message);


    }
    private class GestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        SuggestionActivity.PlayAreaView view;
        public GestureListener(SuggestionActivity.PlayAreaView view) {
            this.view = view;
        }
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            final RelativeLayout rl = (RelativeLayout) findViewById(R.id.constraint);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!firstTouch) {
                        point.setImageResource(R.drawable.pin);
                        xFinal = Math.round(x - xOffset);
                        yFinal = Math.round(y - yOffset);
                        lp.setMargins(x, y, 0, 0);
                        point.setLayoutParams(lp);
                        point.getLayoutParams().height = 70;
                        point.getLayoutParams().width = 40;
                        rl.addView(point);
                        firstTouch = true;
                    }
                    else
                    {

                        xFinal = Math.round(x - xOffset);
                        yFinal = Math.round(y - yOffset);

                        ImageView current = point;

                        current.setLeft(Math.round(x));
                        current.setTop(Math.round(y));
                    }
                    Log.i("TAG", "touched down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    if (firstTouch) {
                        ((ViewGroup)point.getParent()).removeView(point);
                        xFinal = Math.round(x + xOffset);
                        yFinal = Math.round(y + yOffset);
                        lp.setMargins(x, y, 0, 0);
                        point.setLayoutParams(lp);
                        point.getLayoutParams().height = 70;
                        point.getLayoutParams().width = 40;
                        rl.addView(point);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("TAG", "touched up");

                    break;
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return true;
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
            gestures = new GestureDetector(SuggestionActivity.this,
                    new SuggestionActivity.GestureListener(this));
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
    public void confirm(View view) {
        EditText name = findViewById(R.id.classification);
        if (firstTouch && !name.getText().toString().equals(""))
        {

            Log.d("Printer", "Attempting Print");




            File path = this.getFilesDir();
            File file = new File(path,"pins.txt");

            File imgPath = this.getDir("imageDir", Context.MODE_PRIVATE);
            File img = new File(imgPath,name.getText().toString()+".jpg");
            FileOutputStream fos = null;
            try{
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
                out.println(name.getText().toString());
                out.println(message);
                out.println(xFinal);
                out.println(yFinal);
                out.close();

                fos = new FileOutputStream(img);
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                fos.close();

                //MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, name.getText().toString() , message);
                Log.d("Printer", "Print Finished");
                finish();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void cancel(View view)
    {
        finish();
    }


}
