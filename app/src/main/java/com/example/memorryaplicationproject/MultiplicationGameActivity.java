package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MultiplicationGameActivity extends AppCompatActivity {

    private TextView tv_math_score;
    private TextView tv_math_life;
    private TextView tv_math_time;
    private TextView tv_math_question;
    private EditText ed_math_answer;
    private Button bt_math_confirmAnswer;
    private Button bt_math_nextQuestion;
    private Random random = new Random();
    private int number1;
    private int number2;
    private int userAnswer;
    private int correctAnswer;
    private int score = 0;
    private int life = 3;
    private CountDownTimer timer;
    private static final long START_TIME_IN_MILLIS = 120000;
    private Boolean timer_running;
    private long time_left_in_millis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication_game);

        //define components

        tv_math_score = findViewById(R.id.tv_math_score);
        tv_math_life = findViewById(R.id.tv_math_life);
        tv_math_time = findViewById(R.id.tv_math_time);
        tv_math_question = findViewById(R.id.tv_math_question);
        ed_math_answer = findViewById(R.id.ed_math_answer);
        bt_math_confirmAnswer = findViewById(R.id.bt_math_confirmAnswer);
        bt_math_nextQuestion = findViewById(R.id.bt_math_nextQuestion);


        gameContinue();

        bt_math_confirmAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userAnswer = Integer.valueOf(ed_math_answer.getText().toString());

                pauseTimer();

                if(userAnswer == correctAnswer)
                {

                    score = score + 10;
                    tv_math_score.setText("" + score);
                    tv_math_question.setText("Well done, your answer is correct!!!");


                }else{

                    life = life - 1;
                    tv_math_life.setText("" + life);
                    tv_math_question.setText("Sorry, the answer was not right!");

                }


            }
        });

        bt_math_nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ed_math_answer.setText("");
                gameContinue();
                resetTimer();

                if (life == 0){

                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent( MultiplicationGameActivity.this, ResultActivity.class);
                    intent.putExtra("Score", score);
                    startActivity(intent);
                    finish();

                }
                else {

                    gameContinue();

                }

            }
        });
    }
    public void gameContinue(){

        number1 = random.nextInt(100);
        number2 = random.nextInt(100);

        correctAnswer = number1 * number2;

        tv_math_question.setText(number1 + " * " + number2);
        startTimer();


    }

    public void startTimer(){

        timer = new CountDownTimer(time_left_in_millis, 1000) {
            @Override
            public void onTick(long l) {

                time_left_in_millis = l;
                updateTimer();

            }

            @Override
            public void onFinish() {

                timer_running = false;
                pauseTimer();
                resetTimer();
                updateTimer();
                life = life - 1;
                tv_math_life.setText("" +life);
                tv_math_question.setText("Sorry, time is up!");

            }
        }.start();

        timer_running = true;
    }

    public void updateTimer(){

        int second = (int)(time_left_in_millis /1000) % 60;
        String time_left = String.format(Locale.getDefault(), "%02d", second);
        tv_math_time.setText(time_left);

    }

    public void pauseTimer(){

        timer.cancel();
        timer_running = false;

    }

    public void resetTimer(){

        time_left_in_millis = START_TIME_IN_MILLIS;
        updateTimer();

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MultiplicationGameActivity.this,
                MathsGamesActivity.class));
        finish();
    }
}