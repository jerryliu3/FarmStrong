package com.example.jerry.FarmStrong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;

public class DetailView extends AppCompatActivity {

    Hashtable<String, String> solutions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        ImageView img = findViewById(R.id.img);
        TextView name = findViewById(R.id.name);
        TextView classification = findViewById(R.id.classification);
        TextView solution = findViewById(R.id.solution);
        Intent i = getIntent();
        try {
            File imgPath = this.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(imgPath,i.getStringExtra("name")+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img.setImageBitmap(b);


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        solutions = new Hashtable<String,String>();
        solutions.put("Apple: Apple Scab",": One method of apple scab treatment is to apply a fungicide, such as Myclotect™ 2 – 4 times in the spring as the leaves emerge. It is important to know that fungicide applications are preventive only and need to be applied as leaves emerge and then again 14 days later. Fungicides properly applied will be 95% effective for treating apple scab.");
        solutions.put("Apple: Black Rot","Treating black rot on apple trees starts with sanitation. Because fungal spores overwinter on fallen leaves, mummified fruits, dead bark and cankers, it’s important to keep all the fallen debris and dead fruit cleaned up and away from the tree.\n");
        solutions.put("Apple: Cedar Apple Rust"," pply contact fungicide to trees in close proximity to the infected cedar according to the manufacturer’s guidelines. Look for the active ingredient potassium bicarbonate, approved in California for contact fungicide. Spray the trees, including trunk, branches and foliage, when you see yellow spots on the leaves, which often occurs in mid-April.\n");
        solutions.put("Apple: Healthy","You’re safe! There is nothing to worry about on your plant");
        solutions.put("Cherry: Healthy","You’re safe! There is nothing to worry about on your plant");
        solutions.put("Cherry: Powdery Mildew","Manage your irrigation carefully. Perform root sucker management. The key to managing powdery mildew on the fruit is to keep the disease off of the leaves. Most synthetic fungicides are preventative, not eradicative, so be pro-active about disease prevention. Maintain a consistent program from shuck fall through harvest.\n");
        solutions.put("Bell Pepper: Healthy","You’re safe! There is nothing to worry about on your plant\n");
        solutions.put("Bell Pepper: Bacterial Spot","Once the symptoms of bacterial leaf spot begin to appear on your pepper plants, it’s too late to save them. However, if you take precautions before planting next season, you’ll have a better chance of preventing peppery leaf spot problems in the future. Crop rotation can help prevent bacterial leaf spot. Do not plant peppers or tomatoes in a location where either of these crops has been grown in the past four or five years.\n");
        solutions.put("Potato: Early Blight","Treatment of early blight includes prevention by planting potato varieties that are resistant to the disease; late maturing are more resistant than early maturing varieties. Avoid overhead irrigation and allow for sufficient aeration between plants to allow the foliage to dry as quickly as possible. Practice a 2-year crop rotation. That is, do not replant potatoes or other crops in this family for 2 years after a potato crop has been harvested.\n");
        solutions.put("Potato: Healthy","You’re safe! There is nothing to worry about on your plant ");
        solutions.put("Potato: Late Blight","Plant resistant cultivars when available. Remove volunteers from the garden prior to planting and space plants far enough apart to allow for plenty of air circulation. Water in the early morning hours, or use soaker hoses, to give plants time to dry out during the day — avoid overhead irrigation.");
        solutions.put("Corn: Healthy","You’re safe! There is nothing to worry about on your plant ");
        solutions.put("Corn: Common Rust","To reduce the incidence of corn rust, plant only corn that has resistance to the fungus. Resistance is either in the form of race-specific resistance or partial rust resistance. In either case, no sweet corn is completely resistant. If the corn begins to show symptoms of infection, immediately spray with a fungicide. The fungicide is most effective when started at the first sign of infection. Two applications may be necessary.");
        solutions.put("Corn: Northern Leaf Blight","Crop rotation to reduce previous corn residues and disease inoculum. Tillage to help break down crop debris and reduce inoculum load. Fungicide application to reduce yield loss and improve harvestability. Consider hybrid susceptibility, previous crop, tillage, field history, application cost, corn price");
        solutions.put("Corn: Grey Leaf Spot","Crop rotation may reduce disease pressure, but rotation is only a partial solution. Compared to other residue-borne pathogens, gray leaf spot has longer survival time in debris");
        solutions.put("Tomato: Healthy ","healthy- You’re safe! There is nothing to worry about on your plant");
        solutions.put("Tomato: Early Blight","Controlling the environment to prevent excessive heat, moisture, and crowding helps to inhibit the fungal growth. Remove affected leaves and adjust care as necessary.");
        solutions.put("Tomato: Late Blight","Controlling the environment to prevent excessive heat, moisture, and crowding helps to inhibit the fungal growth. Remove affected leaves and adjust care as necessary.");
        solutions.put("Tomato: Bacterial Spot","To treat the problem, remove the infected areas as soon as you see them.");
        solutions.put("Tomato: Leaf Mold", "Let the plants air out. If they are being grown in a green house, air exposure is a must, because the humidity that the fungus needs to survive is dried up.\n" + "If the tomatoes are being grown outside, try to keep from wetting the leaves when you are watering the plants. When you water the plants, earlier watering is optimal because it allows the plant time to dry when the sun comes, keeping humidity around leaves to a minimum.\n");
        solutions.put("Tomato: Septoria Leaf Spot"," Leaf spots are caused by a fungus, encouraged by excess heat and moisture, so removing the affected leaves and adjusting the environment will help to stop the spread.");
        solutions.put("Tomato: Target Spot"," Leaf spots are caused by a fungus, encouraged by excess heat and moisture, so removing the affected leaves and adjusting the environment will help to stop the spread.");
        solutions.put("Tomato: Tomato Mosaic Virus","Fungicide will NOT treat this viral disease, spot treat with least-toxic natural pest control.");
        solutions.put("Tomato: Yellow Leaf Curl Virus","Treatments that are commonly used for this disease include insecticides, hybrid seeds, and growing tomatoes under greenhouse conditions.\n");
        solutions.put("Unknown Class","This is not a crop that is covered by this application");


        Log.d("Intent Output",i.getStringExtra("name") + " "+ i.getStringExtra("classification"));
        name.setText("Name: "+ i.getStringExtra("name"));
        String output = i.getStringExtra("classification");
        classification.setText("Classification: " + output);

        //String type = output.substring(0,output.indexOf(": "));
        String disease = output.substring(0,output.indexOf(" - "));
        Log.d("String Process", disease);
    if(solutions.containsKey(disease))
        solution.setText("Solution: " + solutions.get(disease));
    else
        solution.setText("There are no solutions on file for this class");



    }

    public void back(View view)
    {
        finish();
    }
}
