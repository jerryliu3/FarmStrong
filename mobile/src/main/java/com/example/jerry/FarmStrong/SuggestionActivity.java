package com.example.jerry.FarmStrong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuggestionActivity extends AppCompatActivity {
    TextView classText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        Intent intent = getIntent();
        String message = intent.getStringExtra("class");
        classText = (TextView) findViewById(R.id.suggestion);
        classText.setText("The class of disease found was: " + message);
    }

}
