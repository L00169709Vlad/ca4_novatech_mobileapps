package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MatchNumberActivity extends AppCompatActivity {

    TextView tv_level, tv_number;

    EditText et_number;

    Button b_confirm;



    int currentLevel = 1;
    String generatedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_number);

        tv_level = findViewById(R.id.tv_level);
        tv_number = findViewById(R.id.tv_number);

        et_number = findViewById(R.id.et_number);

        b_confirm = findViewById(R.id.b_confirm);


        //hide the input and the button and show the number
        tv_number.setVisibility(View.VISIBLE);
        et_number.setVisibility(View.GONE);
        b_confirm.setVisibility(View.GONE);

        //display current Level
        tv_level.setText("Level: " + currentLevel);

        //Display random Number according to the level
        generatedNumber = generateNumber(currentLevel);
        tv_number.setText(generatedNumber);

        //display the elements after a second and hide the number
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_number.setVisibility(View.VISIBLE);
                b_confirm.setVisibility(View.VISIBLE);
                tv_number.setVisibility(View.GONE);

                et_number.requestFocus();
            }
        }, 2000);
        b_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if numbers are the same:
                if(generatedNumber.equals(et_number.getText().toString())){
                    //hide the input and the button and show the number
                    tv_number.setVisibility(View.VISIBLE);
                    et_number.setVisibility(View.GONE);
                    b_confirm.setVisibility(View.GONE);

                    //remove text from input:
                    et_number.setText("");

                    //increase level:
                    currentLevel++;
                    //display current Level
                    tv_level.setText("Level: " + currentLevel);


                    //Display random Number according to the level
                    generatedNumber = generateNumber(currentLevel);
                    tv_number.setText(generatedNumber);

                    //display the elements after a second and hide the number
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_number.setVisibility(View.VISIBLE);
                            b_confirm.setVisibility(View.VISIBLE);
                            tv_number.setVisibility(View.GONE);

                            et_number.requestFocus();
                        }
                    }, 2000);

                } else {
                    tv_level.setText("Game Over! The Number was " + generatedNumber);
                    b_confirm.setEnabled(false);

                }
            }
        });
    }


    public static String generateNumber(int digits){
        String output = "";
        Random r = new Random();
        for(int i=0; i<digits; i++){
            int randomDigit = r.nextInt(10);
            output = output + "" + randomDigit;
        }
        return output;
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MatchNumberActivity.this,
                MathsGamesActivity.class));
        finish();
    }
}