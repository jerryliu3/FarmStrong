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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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


    private String name;
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
        takeImage();
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

    public void classify(){
        //do machine learning
        new callAPI().execute();
    }

    public void savePhoto(View view){
        Intent intent = new Intent(CaptureActivity.this, SuggestionActivity.class);
        intent.putExtra("class", resultText.getText());
        intent.putExtra("img",imageBitmap);
        startActivity(intent);
        finish();

    }



    public void takeImage(View view){
        dispatchTakePictureIntent();
    }

    public void takeImage(){
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
            String urlString = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/7368cb8d-56d3-4595-b85d-26e1cc259396/classify/iterations/cross_species_diseases/image";
            String urlString2 = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/580defa3-63f5-48ac-b584-4257b7cf6e7e/classify/iterations/Iteration5/image";
            String url3 = "";
                //First Algorithm
                String[] resultA = getAnswer(getClassification(urlString));
                modelOutput = resultA[0];
                Log.d("Classification","First One Done" + modelOutput);
                //Second Algorithm
                String [] resultB = getAnswer(getClassification(urlString2));
                 double conf1 = Double.parseDouble(resultA[1]);
                String classificationA = resultB[0];
                 Log.d("Classification","Second One Done " +classificationA);

                String type ="";
                if (classificationA.equals("Potato")) {
                    url3 = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/f6bb20b7-5a4f-4923-96f5-904d8658e5e1/classify/iterations/potato-model/image";
                    Log.d("ClassificationA", classificationA + " " + "Potato");
                }
                else if (classificationA.equals("Tomato")) {
                    url3 = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/c9ec68ae-8bd9-4675-b161-49678e021ff8/classify/iterations/tomato-model/image";
                    Log.d("ClassificationA", classificationA + " " + "Tomato");
                }
                else if (classificationA.equals("Corn")) {
                    url3 = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/b0362215-67da-42ac-850f-0b648896e801/classify/iterations/Iteration1/url";
                    Log.d("ClassificationA", classificationA + " " + "Corn");
                }
                else if (classificationA.equals("Peach")) {
                    url3 = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/164d24eb-a697-404a-b5cc-e3a14a956914/classify/iterations/Iteration1/url";
                    Log.d("ClassificationA", classificationA + " " + "Peach");
                }
                else {
                    Log.d("ClassificationA", classificationA + " " + "skipped");
                    return modelOutput + " - " + String.format("%.2f",conf1*100) + "%";

                }


                //Third Algo
                String[] resultC = getAnswer(getClassification(url3));
                String classificationB = resultC[0];

            Log.d("Classification","Third One Done" + " " + classificationB);



                double conf2 = Double.parseDouble(resultB[1]);
                double conf3 = Double.parseDouble(resultC[1]);
                Log.d("Confidence", conf2 + " " + conf3 + "=" + conf2*conf3);
                if (conf2*conf3 > conf1 )
                    return classificationB + " - " + String.format("%.2f",conf2*conf3*100) + "%";
                else
                    return modelOutput + " - " + String.format("%.2f",conf1*100) + "%";

        }


        public String[] getAnswer(String modelOutput2){

            ArrayList <Double> probabilities = new <Double>ArrayList();
            ArrayList <String> tags = new <Integer>ArrayList();
            Log.e("Help",modelOutput2);
            while(modelOutput2.indexOf("probability") != -1) {
                modelOutput2 = modelOutput2.substring(modelOutput2.indexOf("probability") + 11);
                Log.e("Help", modelOutput2);

                int p1 = modelOutput2.indexOf(":") + 1;
                int p2 = modelOutput2.indexOf(",");
                probabilities.add(Double.parseDouble(modelOutput2.substring(p1, p2)));
                Log.e("Help", modelOutput2.substring(p1, p2));

                modelOutput2 = modelOutput2.substring(modelOutput2.indexOf("tagName") + 7);
                Log.e("Help", modelOutput2);

                int t1 = modelOutput2.indexOf(":") + 2;
                int t2 = modelOutput2.indexOf("}") - 1;
                tags.add(modelOutput2.substring(t1, t2));
                Log.e("Help", modelOutput2.substring(t1, t2));
            }
            Log.d("Answer",tags.get(0) + " " + (double) Math.round(probabilities.get(0)));
            String temp = tags.get(0);
            if (tags.get(0).equals("Negative"))
            {
                temp = "Unknown Class";
            }
            return new String[] {tags.get(0), ""+ (double) probabilities.get(0)};
            }

        public String getClassification(String apiLink)
        {

            try{
            URL url = new URL(apiLink);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            String answer = "";
            urlConnection.setRequestProperty("Prediction-Key", "167bbe0980344a2bb1e04484c5b53485");
            urlConnection.setRequestProperty("Content-Type", "image/jpeg");
            Log.e("ImageUploader", "URL Connection: Success");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            Log.e("ImageUploader", "URL Connection: Success");
            OutputStream output = urlConnection.getOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.close();
            Log.e("ImageUploader", "URL Connection: Success");
            Scanner s = new Scanner(urlConnection.getInputStream());
            answer = s.nextLine();
            Log.e("ImageUploader", "Answer: " + answer);
            s.close();
            Log.e("ImageUploader", "URL Connection: Success");
            urlConnection.disconnect();
            return answer;
            }

            catch (IOException e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            modelOutput = s;

            /*ArrayList <Double> probabilities = new <Double>ArrayList();
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

            //int size = Math.min(3, probabilities.size());
            String result = "";
            //for(int x=0;x<size;x++){
                result += tags.get(0) + ": " + (double) Math.round(probabilities.get(0) * 100) + "%\n";
            //} */
            resultText.setText(s);
            pd.dismiss();
        }


    }

}
