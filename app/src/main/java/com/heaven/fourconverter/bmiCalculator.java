package com.heaven.fourconverter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class bmiCalculator extends AppCompatActivity {

    private EditText weightInput, heightInput;
    private Spinner weightUnitSpinner, heightUnitSpinner;
    private Button calculateButton;
    private LinearLayout animation;
    private TextView resultDisplay, bmiDisplay;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private LottieAnimationView celeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);

        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        weightUnitSpinner = findViewById(R.id.weightUnitSpinner);
        heightUnitSpinner = findViewById(R.id.heightUnitSpinner);
        calculateButton = findViewById(R.id.calculateButton);
        resultDisplay = findViewById(R.id.resultDisplay);
        bmiDisplay = findViewById(R.id.bmiDisplay);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        celeb = findViewById(R.id.celeb);
        animation= findViewById(R.id.animation);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(this,
                R.array.weight_units, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> heightAdapter = ArrayAdapter.createFromResource(this,
                R.array.height_units, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        weightUnitSpinner.setAdapter(weightAdapter);
        heightUnitSpinner.setAdapter(heightAdapter);

        weightUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle unit selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no unit selected
            }
        });

        heightUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle unit selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle no unit selected
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double bmi = calculateBMI(); // Capture the returned BMI value
                if (bmi == -1) return; // If BMI is invalid, do not proceed
                celeb.setVisibility(View.VISIBLE);
                animation.setVisibility(View.GONE);
                resultDisplay.setVisibility(View.VISIBLE);
                displayBMIResult(bmi); // Pass the BMI value here

                // Vibrate the phone
                if (vibrator != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(500); // Deprecated in API 26
                    }
                }

                // Play sound
                if (mediaPlayer != null) {
                    mediaPlayer.start(); // Start playing the sound
                }
            }
        });
    }

    private double calculateBMI() {
        String weightStr = weightInput.getText().toString();
        String heightStr = heightInput.getText().toString();

        if (weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Please enter both weight and height", Toast.LENGTH_SHORT).show();
            return -1; // Return an invalid BMI value
        }

        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr);

        String weightUnit = weightUnitSpinner.getSelectedItem().toString();
        String heightUnit = heightUnitSpinner.getSelectedItem().toString();

        if (weightUnit.equals("Select unit") || heightUnit.equals("Select unit")) {
            Toast.makeText(this, "Please select both weight and height units", Toast.LENGTH_SHORT).show();
            return -1; // Return an invalid BMI value
        }

        // Convert weight to kg if necessary
        switch (weightUnit) {
            case "Pounds":
                weight = weight * 0.453592; // convert lbs to kg
                break;
            case "Kilograms":
                // weight is already in kg, no conversion needed
                break;
            default:
                resultDisplay.setText("Invalid weight unit");
                resultDisplay.setVisibility(View.VISIBLE);
                return -1; // Return an invalid BMI value
        }

        // Convert height to meters if necessary
        switch (heightUnit) {
            case "Feet":
                height = height * 0.3048; // convert feet to meters
                break;
            case "Centimeters":
                height = height / 100; // convert cm to meters
                break;
            case "Inches":
                height = height * 0.0254; // convert inches to meters
                break;
            default:
                resultDisplay.setText("Invalid height unit");
                return -1; // Return an invalid BMI value
        }

        // Check for valid height to avoid division by zero
        if (height == 0) {
            resultDisplay.setText("Height cannot be zero");
            return -1; // Return an invalid BMI value
        }

        // Calculate BMI
        double bmi = weight / (height * height);
        resultDisplay.setText(String.format("Your BMI is: %.2f", bmi));

        return bmi; // Return the calculated BMI value
    }

    private void displayBMIResult(double bmi) {
        String bmiResult;
        if (bmi < 18.5) {
            bmiResult = "Your BMI is low: " + String.format("%.2f", bmi);
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            bmiResult = "Your BMI is normal: " + String.format("%.2f", bmi);
        } else {
            bmiResult = "Your BMI is high: " + String.format("%.2f", bmi);
        }
        bmiDisplay.setText(bmiResult);
        bmiDisplay.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(bmiCalculator.this, mainScreen.class);
        startActivity(intent);
        finish(); // Optionally call finish if you don't want to keep this activity in the back stack
    }
}
