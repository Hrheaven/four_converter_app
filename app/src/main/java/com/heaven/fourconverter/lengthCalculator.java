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

import com.airbnb.lottie.LottieAnimationView;

public class lengthCalculator extends AppCompatActivity {

    private EditText inputLength;
    private Spinner inputUnitSpinner, outputUnitSpinner;
    private LinearLayout animation;
    private Button convertButton;
    private TextView resultDisplay;
    private LottieAnimationView celeb;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length_calculator);

        inputLength = findViewById(R.id.inputLength);
        inputUnitSpinner = findViewById(R.id.inputUnitSpinner);
        outputUnitSpinner = findViewById(R.id.outputUnitSpinner);
        convertButton = findViewById(R.id.convertButton);
        resultDisplay = findViewById(R.id.resultDisplay);
        celeb=findViewById(R.id.celeb);
        animation= findViewById(R.id.animation);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.length_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputUnitSpinner.setAdapter(adapter);
        outputUnitSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertLength();
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

    private void convertLength() {
        String lengthStr = inputLength.getText().toString().trim();

        if (lengthStr.isEmpty()) {
            Toast.makeText(this, "Please enter a length", Toast.LENGTH_SHORT).show();
            return;
        }

        double length = Double.parseDouble(lengthStr);

        String inputUnit = inputUnitSpinner.getSelectedItem().toString();
        String outputUnit = outputUnitSpinner.getSelectedItem().toString();

        double lengthInMeters = convertToMeters(length, inputUnit);
        final double convertedLength = convertFromMeters(lengthInMeters, outputUnit);

        // Show "Calculating" message
        resultDisplay.setText("Calculating...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayResult(convertedLength, outputUnit);
            }
        }, 1000); // 1 second delay
    }

    private double convertToMeters(double length, String unit) {
        switch (unit) {
            case "Kilometers":
                return length * 1000;
            case "Meters":
                return length;
            case "Centimeters":
                return length / 100;
            case "Millimeters":
                return length / 1000;
            case "Feet":
                return length * 0.3048;
            case "Inches":
                return length * 0.0254;
            default:
                return length;
        }
    }

    private double convertFromMeters(double lengthInMeters, String unit) {
        switch (unit) {
            case "Kilometers":
                return lengthInMeters / 1000;
            case "Meters":
                return lengthInMeters;
            case "Centimeters":
                return lengthInMeters * 100;
            case "Millimeters":
                return lengthInMeters * 1000;
            case "Feet":
                return lengthInMeters / 0.3048;
            case "Inches":
                return lengthInMeters / 0.0254;
            default:
                return lengthInMeters;
        }
    }

    private void displayResult(double length, String unit) {
        String result = String.format("Converted length: %.2f %s", length, unit);
        resultDisplay.setText(result);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(lengthCalculator.this, mainScreen.class);
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
