package com.example.jerry.FarmStrong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Output;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SuggestionActivity extends AppCompatActivity {
    TextView classText;
    boolean firstTouch = false;
    ImageView point;
    int xFinal;
    int yFinal;
    String message;
    Bitmap imageBitmap;

    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();
            point = new ImageView(getApplicationContext());
            final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!firstTouch) {
                        point.setImageResource(R.drawable.pin);
                        xFinal = x;
                        yFinal = y;
                        lp.setMargins(x, y, 0, 0);
                        point.setLayoutParams(lp);
                        point.getLayoutParams().height = 70;
                        point.getLayoutParams().width = 40;
                        rl.addView(point);
                        firstTouch = true;
                    }
                    Log.i("TAG", "touched down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    if (firstTouch) {
                        lp.setMargins(x, y, 0, 0);
                        point.setLayoutParams(lp);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("TAG", "touched up");

                    break;
            }
            point.requestLayout();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        Intent intent = getIntent();
        imageBitmap = intent.getParcelableExtra("img");
        message = intent.getStringExtra("class");
        ImageView farm = findViewById(R.id.farm);
        farm.setOnTouchListener(handleTouch);

        //classText.setText("The class of disease found was: " + message);


    }

    public void confirm(View view) {
        if (firstTouch)
        {

            Log.d("Printer", "Attempting Print");


            EditText name = findViewById(R.id.name);

            File path = this.getFilesDir();
            File file = new File(path,"pins.txt");

            File imgPath = this.getDir("imageDir", Context.MODE_PRIVATE);
            File img = new File(imgPath,name.getText().toString()+".jpg");
            FileOutputStream fos = null;
            try{
                PrintWriter out = new PrintWriter(file);
                out.println(name.getText().toString());
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
