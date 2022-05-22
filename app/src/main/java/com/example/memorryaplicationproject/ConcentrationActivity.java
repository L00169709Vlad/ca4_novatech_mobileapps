package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConcentrationActivity extends AppCompatActivity {

    private Button b_3x3_game;
    private Button b_4x4_game;
    private Button b_5x5_game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentration);

        b_3x3_game=findViewById(R.id.b_3x3_game);
        b_4x4_game=findViewById(R.id.b_4x4_game);
        b_5x5_game=findViewById(R.id.b_5x5_game);

        b_3x3_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        b_4x4_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConcentrationActivity.this, Game4x4Activity.class));
                finish();
            }
        });

        b_5x5_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ConcentrationActivity.this,
                GamesActivity.class));
        finish();
    }
}