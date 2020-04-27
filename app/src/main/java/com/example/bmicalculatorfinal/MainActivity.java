package com.example.bmicalculatorfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private EditText height;
    private EditText weight;
    private TextView score;
    private Button gym;
    private String word;
    TextView myText;
    ImageButton myVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);

        speak();
        weight.setText(word,TextView.BufferType.EDITABLE);
        height.setText(word,TextView.BufferType.EDITABLE);
        score = (TextView) findViewById(R.id.score);
        gym = (Button) findViewById(R.id.gyms);
        gym.setVisibility(View.INVISIBLE);
        myText = findViewById(R.id.textTv);
        myVoice = findViewById(R.id.voiceBtn);
        myVoice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                speak();
            }
        });
    }
    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi say something");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            Toast.makeText(this, "Voice recognizer is not available in your device", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "OK. Voice recognizer is available.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    myText.setText(result.get(0));
                    word = result.get(0);
                }
                break;
            }
        }
    }


    public void calculateBMI(View a) {
        String yourHeight = height.getText().toString();
        String yourWeight = weight.getText().toString();
        if (yourHeight != null && !"".equals(yourHeight)
                && yourWeight != null && !"".equals(yourWeight)) {
            float valueOfHeight = Float.parseFloat(yourHeight);
            float valueOfWeight = Float.parseFloat(yourWeight);
            float bMIcalculator = valueOfWeight / (valueOfHeight * valueOfHeight) * 703;
            displayBMI(bMIcalculator);
        }
    }
    private void displayBMI(float bMIcalculator) {
        String label = "";
        gym = (Button) findViewById(R.id.gyms);
        if (Float.compare(bMIcalculator, 15f) <= 0) {
            label = getString(R.string.extremely_underweight);
            gym.setVisibility(View.VISIBLE);
        } else if (Float.compare(bMIcalculator, 15f) > 0 && Float.compare(bMIcalculator, 16f) <= 0) {
            label = getString(R.string.moderately_underweight);
            gym.setVisibility(View.VISIBLE);
        } else if (Float.compare(bMIcalculator, 16f) > 0 && Float.compare(bMIcalculator, 18.5f) <= 0) {
            label = getString(R.string.underweight);
            gym.setVisibility(View.VISIBLE);
        } else if (Float.compare(bMIcalculator, 18.5f) > 0 && Float.compare(bMIcalculator, 25f) <= 0) {
            label = getString(R.string.normal);
            gym.setVisibility(View.INVISIBLE);
        } else if (Float.compare(bMIcalculator, 25f) > 0 && Float.compare(bMIcalculator, 30f) <= 0) {
            label = getString(R.string.overweight);
            gym.setVisibility(View.VISIBLE);
        } else if (Float.compare(bMIcalculator, 30f) > 0 && Float.compare(bMIcalculator, 35f) <= 0) {
            label = getString(R.string.slightly_obese);
            gym.setVisibility(View.VISIBLE);
        } else if (Float.compare(bMIcalculator, 35f) > 0 && Float.compare(bMIcalculator, 40f) <= 0) {
            label = getString(R.string.very_obese);
            gym.setVisibility(View.VISIBLE);
        } else {
            label = getString(R.string.extremely_obese);
            gym.setVisibility(View.VISIBLE);
        }
        label = bMIcalculator + "\n\n" + label;
        score.setText(label);
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapActivity();
            }
        });
    }

    public void startMapActivity() {
        Intent intent = new Intent(this,  MapsActivity.class);
        startActivity(intent);
    }
}