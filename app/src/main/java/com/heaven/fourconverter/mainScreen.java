package com.heaven.fourconverter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class mainScreen extends AppCompatActivity {

    CardView btnBmi, btnLength, btnWeight, btnAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //----------------code start from here----------------

        btnBmi = findViewById(R.id.btnBmi);
        btnLength = findViewById(R.id.btnLength);
        btnWeight = findViewById(R.id.btnWeight);
        btnAge = findViewById(R.id.btnAge);

        btnBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bmiPage= new Intent(mainScreen.this, bmiCalculator.class);
                startActivity(bmiPage);
                finish();
            }
        });

        btnLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bmiPage= new Intent(mainScreen.this, lengthCalculator.class);
                startActivity(bmiPage);
                finish();
            }
        });

        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bmiPage= new Intent(mainScreen.this, weightCalculator.class);
                startActivity(bmiPage);
                finish();
            }
        });

        btnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bmiPage= new Intent(mainScreen.this, ageCalculator.class);
                startActivity(bmiPage);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(mainScreen.this)
                .setTitle("Confirm Exit")
                .setMessage("Do you Really Want to exit?")
                .setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finishAndRemoveTask();
                    }
                })
                .show();
    }
}