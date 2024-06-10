package com.heaven.fourconverter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;

public class weightCalculator extends AppCompatActivity {

    private EditText inputWeight;
    private LinearLayout animation;
    private Spinner inputUnitSpinner, outputUnitSpinner;
    private Button convertButton;
    private TextView resultDisplay;
    private LottieAnimationView celeb;
    private Vibrator vibrator;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_calculator);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputWeight = findViewById(R.id.inputWeight);
        inputUnitSpinner = findViewById(R.id.inputUnitSpinner);
        outputUnitSpinner = findViewById(R.id.outputUnitSpinner);
        convertButton = findViewById(R.id.convertButton);
        resultDisplay = findViewById(R.id.resultDisplay);
        animation= findViewById(R.id.animation);
        celeb=findViewById(R.id.celeb);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weight_Units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputUnitSpinner.setAdapter(adapter);
        outputUnitSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertWeight();
                resultDisplay.setVisibility(View.VISIBLE); // Corrected typo and completed the method call
                celeb.setVisibility(View.VISIBLE);
                animation.setVisibility(View.GONE);

                if (vibrator != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(500); // Deprecated in API 26
                    }
                }
                if (mediaPlayer != null) {
                    mediaPlayer.start(); // Start playing the sound
                }
            }
        });  
    }

    private void convertWeight() {
        String weightStr = inputWeight.getText().toString().trim();

        if (weightStr.isEmpty()) {
            Toast.makeText(this, "Please enter a weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightStr);

        String inputUnit = inputUnitSpinner.getSelectedItem().toString();
        String outputUnit = outputUnitSpinner.getSelectedItem().toString();

        double weightInKilograms = convertToKilograms(weight, inputUnit);
        final double convertedWeight = convertFromKilograms(weightInKilograms, outputUnit);

        // Show "Calculating" message
        resultDisplay.setText("Calculating...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayResult(convertedWeight, outputUnit);
            }
        }, 1000); // 1 second delay
    }

    private double convertToKilograms(double weight, String unit) {
        switch (unit) {
            case "Kilograms":
                return weight;
            case "Grams":
                return weight / 1000;
            case "Pounds":
                return weight * 0.453592;
            case "Ounces":
                return weight * 0.0283495;
            default:
                return weight;
        }
    }

    private double convertFromKilograms(double weightInKilograms, String unit) {
        switch (unit) {
            case "Kilograms":
                return weightInKilograms;
            case "Grams":
                return weightInKilograms * 1000;
            case "Pounds":
                return weightInKilograms / 0.453592;
            case "Ounces":
                return weightInKilograms / 0.0283495;
            default:
                return weightInKilograms;
        }
    }

    private void displayResult(double weight, String unit) {
        String result = String.format("Converted weight: %.2f %s", weight, unit);
        resultDisplay.setText(result);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(weightCalculator.this, mainScreen.class);
        startActivity(intent);
        finish(); // Optionally call finish if you don't want to keep this activity in the back stack
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the MediaPlayer
            mediaPlayer = null;
        }
    }


}

