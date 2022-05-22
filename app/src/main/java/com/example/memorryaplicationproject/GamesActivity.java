package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GamesActivity extends AppCompatActivity {

    private Button bt_matchNumber;
    private Button bt_matchColour;
    private Button bt_concentration;
    private Button bt_arrangeLetters;
    private Button bt_puzzleGame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        bt_matchNumber = findViewById(R.id.bt_matchNumber);
        bt_matchColour = findViewById(R.id.bt_matchColour);
        bt_concentration = findViewById(R.id.bt_concentration);
        bt_arrangeLetters = findViewById(R.id.bt_arrangeLetters);
        bt_puzzleGame = findViewById(R.id.bt_puzzle);

        bt_matchNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamesActivity.this,
                        MathsGamesActivity.class));
                finish();
            }
        });

        bt_matchColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamesActivity.this,
                        MatchColourActivity.class));
                finish();
            }
        });

        bt_concentration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamesActivity.this,
                        ConcentrationActivity.class));
                finish();
            }
        });

        bt_arrangeLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamesActivity.this,
                        ArrangeLetterActivity.class));
                finish();
            }
        });

        bt_puzzleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamesActivity.this,
                        PuzzleGame.class));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(GamesActivity.this,
                MainMenuActivity.class));
        finish();
    }
}