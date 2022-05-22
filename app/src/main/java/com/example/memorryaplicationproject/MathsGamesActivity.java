package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MathsGamesActivity extends AppCompatActivity {

    private Button bt_addition;
    private Button bt_subtraction;
    private Button bt_multiplication;
    private Button bt_numberMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_games);

        bt_addition = findViewById(R.id.bt_addition);
        bt_subtraction = findViewById(R.id.bt_subtraction);
        bt_multiplication = findViewById(R.id.bt_multiplication);
        bt_numberMatch = findViewById(R.id.bt_numberMatch);

        bt_addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MathsGamesActivity.this,
                        AdditionGameActivity.class));
                finish();

            }
        });

        bt_subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MathsGamesActivity.this,
                        SubtractionGameActivity.class));
                finish();

            }
        });

        bt_multiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MathsGamesActivity.this,
                        MultiplicationGameActivity.class));
                finish();

            }
        });

        bt_numberMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MathsGamesActivity.this,
                        MatchNumberActivity.class));
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MathsGamesActivity.this,
                GamesActivity.class));
        finish();
    }
}