package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MatchColourActivity extends AppCompatActivity {

    ImageView iv_button, iv_arrow;
    TextView tv_points;
    ProgressBar progressBar;

    Handler handler;
    Runnable runnable;

    Random r;

    private final static int STATE_RED = 1;
    private final static int STATE_GREEN = 2;
    private final static int STATE_PINK = 3;
    private final static int STATE_BROWN = 4;
    private final static int STATE_YELLOW = 5;
    private final static int STATE_PURPLE = 6;
    private final static int STATE_BLUE = 7;
    private final static int STATE_TEAL = 8;

    int buttonState = STATE_BLUE;
    int arrowState = STATE_BLUE;

    int currentTime = 4000;
    int startTime = 4000;

    int currentPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_colour);

        iv_button = findViewById(R.id.iv_button);
        iv_arrow = findViewById(R.id.iv_arrow);
        tv_points = findViewById(R.id.tv_points);
        progressBar = findViewById(R.id.progressBar);

        //set the initial progress time to 4s
        progressBar.setMax(startTime);
        progressBar.setProgress(startTime);

        //display the points
        tv_points.setText("Points: " + currentPoints);

        //generate random arrow colour at the start of the game
        r= new Random();
        arrowState= r.nextInt(8) +1;
        setArrowImage(arrowState);

        iv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rotate the button with the colours
                setButtonImage(setButtonPosition(buttonState));
            }
        });

        // main game loop
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                //show progress
                currentTime = currentTime - 50;
                progressBar.setProgress(currentTime);
                // check the progress bar time
                if (currentTime > 0) {
                    handler.postDelayed(runnable,100);
                } else{
                    //check if colours of the arrow and button match
                    if(buttonState == arrowState){
                        // increase and show the points
                        currentPoints = currentPoints + 1;
                        tv_points.setText("Points: " + currentPoints);

                        //make speed higher as progressing
                        startTime = startTime - 100;
                        //if the speed gets to 1 s make it back to 2 s
                        if(startTime < 1000) {
                            startTime = 2000;
                        }
                        progressBar.setMax(startTime);
                        currentTime = startTime;
                        progressBar.setProgress(currentTime);

                        //generate new color of the pointer
                        arrowState = r.nextInt(8) +1;
                        setArrowImage(arrowState);

                        handler.postDelayed(runnable, 100);
                    } else {
                        iv_button.setEnabled(false);
                        Toast.makeText(MatchColourActivity.this, "Game Over!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        //start the game loop
        handler.postDelayed(runnable, 100);

    }
    //display the colour according to the generator
    private void setArrowImage(int state){
        switch (state){
            case STATE_RED:
                iv_arrow.setImageResource(R.drawable.ic_red1);
                arrowState = STATE_RED;
                break;
            case STATE_BLUE:
                iv_arrow.setImageResource(R.drawable.ic_blue1);
                arrowState = STATE_BLUE;
                break;
            case STATE_GREEN:
                iv_arrow.setImageResource(R.drawable.ic_green1);
                arrowState = STATE_GREEN;
                break;
            case STATE_YELLOW:
                iv_arrow.setImageResource(R.drawable.ic_yellow1);
                arrowState = STATE_YELLOW;
                break;
            case STATE_PINK:
                iv_arrow.setImageResource(R.drawable.ic_pink1);
                arrowState = STATE_PINK;
                break;
            case STATE_TEAL:
                iv_arrow.setImageResource(R.drawable.ic_orange1);
                arrowState = STATE_TEAL;
                break;
            case STATE_PURPLE:
                iv_arrow.setImageResource(R.drawable.ic_purple1);
                arrowState = STATE_PURPLE;
                break;
            case STATE_BROWN:
                iv_arrow.setImageResource(R.drawable.ic_brown1);
                arrowState = STATE_BROWN;
                break;
        }
    }

    //rotate animation of the button when clicked
    private void setRotation( ImageView image, int drawable){
        //rotate 45 degrees
        RotateAnimation rotateAnimation = new RotateAnimation(0, -45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(100);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setImageResource(drawable);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image.startAnimation(rotateAnimation);
    }

    //set button colors position 1-8
    public static int setButtonPosition (int position){
        position = position +1;
        if(position >= 9){
            position = 1;
        }
        return position;
    }

    private void setButtonImage(int state) {
        switch (state){
            case STATE_RED:
                setRotation(iv_button, R.drawable.ic_button_red3);
                buttonState = STATE_RED;
                break;
            case STATE_BLUE:
                setRotation(iv_button, R.drawable.ic_button_blue3);
                buttonState = STATE_BLUE;
                break;
            case STATE_GREEN:
                setRotation(iv_button, R.drawable.ic_button_green3);
                buttonState = STATE_GREEN;
                break;
            case STATE_YELLOW:
                setRotation(iv_button, R.drawable.ic_button_yellow3);
                buttonState = STATE_YELLOW;
                break;
            case STATE_PINK:
                setRotation(iv_button, R.drawable.ic_button_pink3);
                buttonState = STATE_PINK;
                break;
            case STATE_TEAL:
                setRotation(iv_button, R.drawable.ic_button_orange3);
                buttonState = STATE_TEAL;
                break;
            case STATE_PURPLE:
                setRotation(iv_button, R.drawable.ic_button_purple3);
                buttonState = STATE_PURPLE;
                break;
            case STATE_BROWN:
                setRotation(iv_button, R.drawable.ic_button_brown3);
                buttonState = STATE_BROWN;
                break;
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MatchColourActivity.this,
                GamesActivity.class));
        finish();
    }

}