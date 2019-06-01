package com.example.jerry.FarmStrong;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class CaptureActivity extends Activity
{
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Button capture;
    private Bitmap imageBitmap;
    private TextView resultText;
    private String modelOutput;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        capture = (Button) findViewById(R.id.capture);
        imageView = (ImageView) findViewById(R.id.imageView);
        resultText = (TextView) findViewById(R.id.resultText);
        modelOutput = "";
        checkCameraPermission();
    }

    public void checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                //have some message
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void classify(View view){
        //do machine learning
        new callAPI().execute();
     }

    public void takeImage(View view){
        dispatchTakePictureIntent();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }


    class callAPI extends AsyncTask<String, Void, String> {
        ProgressDialog pd = new ProgressDialog(CaptureActivity.this); //Android library that we imported.

        @Override
        protected void onPreExecute(){
            super.onPreExecute(); //Uses preexecute from the parent (async) class.
            //Pre process
            pd.setTitle("Please wait...");
            pd.show();
        }
        @Override
        protected String doInBackground(String... params){
            String urlString = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/4cb2231d-c499-40b2-a97b-5d1d03321607/classify/iterations/First%20Model%3A%20FarmStrong/image";
            try{
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                String answer = "";
                urlConnection.setRequestProperty("Prediction-Key", "167bbe0980344a2bb1e04484c5b53485");
                urlConnection.setRequestProperty("Content-Type", "image/jpeg");
                Log.e("ImageUploader", "URL Connection: Success");

                //urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                Log.e("ImageUploader", "URL Connection: Success");

                //urlConnection.connect();
                OutputStream output = urlConnection.getOutputStream();
                //ByteArrayOutputStream bos = new ByteArrayOutputStream(output);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
                //byte[] data = bos.toByteArray();

                //MultipartEntity entity = new MultipartEntity();
                output.close();
                Log.e("ImageUploader", "URL Connection: Success");

                Scanner s = new Scanner(urlConnection.getInputStream());
                answer = s.nextLine();
                Log.e("ImageUploader", "Answer: " + answer);
                s.close();
                Log.e("ImageUploader", "URL Connection: Success");

                urlConnection.disconnect();

                /*urlConnection = (HttpURLConnection)url.openConnection();
                Log.e("ImageUploader", "URL Reconnection: Success");

                urlConnection.setRequestProperty("Prediction-Key", "16ab01ac6f7f43208676bb27813897fc");
                urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
                urlConnection.setRequestMethod("GET");
                Log.e("ImageUploader", "URL Connection: Success");

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = r.readLine()) != null)
                {
                    sb.append(line);
                }
                Log.e("ImageUploader", "URL Connection: Success");

                answer = sb.toString();*/
                urlConnection.disconnect();
                return answer;
            }
            catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            modelOutput = s;

            ArrayList <Double> probabilities = new <Double>ArrayList();
            ArrayList <String> tags = new <Integer>ArrayList();
            Log.e("Help",modelOutput);
            while(modelOutput.indexOf("probability") != -1){
                modelOutput = modelOutput.substring(modelOutput.indexOf("probability") + 11);
                Log.e("Help",modelOutput);

                int p1 = modelOutput.indexOf(":")+1;
                int p2 = modelOutput.indexOf(",");
                probabilities.add(Double.parseDouble(modelOutput.substring(p1, p2)));
                Log.e("Help",modelOutput.substring(p1, p2));

                modelOutput = modelOutput.substring(modelOutput.indexOf("tagName") + 7);
                Log.e("Help",modelOutput);

                int t1 = modelOutput.indexOf(":") + 2;
                int t2 = modelOutput.indexOf("}") - 1;
                tags.add(modelOutput.substring(t1, t2));
                Log.e("Help",modelOutput.substring(t1, t2));

            }

            int size = Math.min(3, probabilities.size());
            String result = "";
            for(int x=0;x<size;x++){
                result += tags.get(x) + ": " + (double) Math.round(probabilities.get(x) * 100) / 100 + "\n";
            }
            resultText.setText(result);

            pd.dismiss();
        }

    }

}
