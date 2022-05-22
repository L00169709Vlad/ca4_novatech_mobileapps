package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView tv_mathResult;
    private Button bt_math_playAgain, bt_math_exit;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tv_mathResult = findViewById(R.id.tv_mathResult);
        bt_math_playAgain = findViewById(R.id.bt_math_playAgain);
        bt_math_exit = findViewById(R.id.bt_math_exit);

        Intent intent = getIntent();
        score = intent.getIntExtra("Score", 0);
        String userScore = String.valueOf(score);
        tv_mathResult.setText("Your Score: " + userScore);

        bt_math_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ResultActivity.this,
                        MathsGamesActivity.class));
                finish();

            }
        });

        bt_math_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ResultActivity.this,
                        MainMenuActivity.class));
                finish();

            }
        });
    }
}