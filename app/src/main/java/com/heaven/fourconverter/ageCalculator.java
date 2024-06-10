package com.heaven.fourconverter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class ageCalculator extends AppCompatActivity {

    private DatePicker datePicker;
    private Button calculateButton;
    private TextView resultDisplay;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculator);

        datePicker = findViewById(R.id.datePicker);
        calculateButton = findViewById(R.id.calculateButton);
        resultDisplay = findViewById(R.id.resultDisplay);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAge();
                resultDisplay.setVisibility(View.VISIBLE);

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
    private void calculateAge() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month, day);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        // Show "Calculating" message
        resultDisplay.setText("Calculating...");
        int finalAge = age;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayResult(finalAge);
            }
        }, 1000); // 1 second delay
    }
    private void displayResult(int age) {
        String result = String.format("Your age is: %d years", age);
        resultDisplay.setText(result);
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
        Intent intent = new Intent(ageCalculator.this, mainScreen.class);
        startActivity(intent);
        finish(); // Optionally call finish if you don't want to keep this activity in the back stack
    }

}
