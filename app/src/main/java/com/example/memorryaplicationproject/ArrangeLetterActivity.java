package com.example.memorryaplicationproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ArrangeLetterActivity extends AppCompatActivity {

    TextView tv_info, tv_word;

    EditText et_guess;

    Button b_check, b_new;

    Random r;

    String currentWord;
//positive words
    String[] dictionary = {
            //A
            "able", "absolutely","abundance", "accepted", "acclaimed", "accommodate", "accomplish", "achievement",
            "action","active","admire","adorable","adventure","affirmative","agree","agreeable","amazing",
            "angelic","appreciate","attentive","attractive","aware","awesome",

            //B
            "balanced", "beaming","beautiful", "believe", "belong", "beneficial", "benevolent", "better",
            "betterment","bliss","blossom","bountiful ",

            //C
            "calm", "capable","captivate", "care", "careful", "celebrated", "champ", "champion",
            "charming","clarity","clean","courageous","creative","curiosity","cute","cool","compassion",

            //D
            "delicate", "delight","delightful", "deserve", "determined", "disciplined", "divine", "dream",

            //E
            "easy", "effective","efficient", "elegant", "encouraging", "excellent", "energized", "energetic",

            //F
            "fabulous", "fair","fantastic", "fine", "flourishing", "focus", "fortunate", "freedom",
            "fresh","friendly","fun",

            //G
            "gallant", "generous","genius", "genuine", "glamorous", "glorious", "glowing", "gorgeous",
            "graceful","gratitude",

            //H
            "handsome", "happy","harmonious", "healing", "healthy", "heavenly", "honorable", "honored",
            "hopeful",

            //I
            "ideal", "illuminate","imaginative", "improve", "independent", "insightful", "inspire", "intelligent",
            "inclusive","immaculate",

            //J
            "jovial", "joy","jubilant ", "deserve", "justice",

            //L
            "laugh", "leader","legendary", "liberate", "light", "like", "lively", "longevity",
            "lovely","lovable","lucky","luminous",

            //M
            "magical", "magnificent","majestic", "marvelous", "mastery", "meaningful", "meditation", "merit",
            "meritorious","motivating","miraculous",

            //N
            "natural", "new","nice", "novel", "nurturing", "nutritious",

            //O
            "observant", "oneness","openhearted", "openminded", "optimistic", "organized",

            //P
            "paradise", "perfect","persistent", "playful", "plentiful", "pleasant", "positive", "powerful",
            "pretty","proud","protected","productive","progress","popular",

            //Q
            "quality", "quotable ",

            //R
            "radiant", "rational","ready", "refined", "refreshing", "reliable", "remarkable", "resourceful",
            "right","reward","radiate",

            //S
            "safe", "scrumptious","secure", "sensational", "sensible", "shine", "simplify", "skilled",
            "smile","soulful","sparkling","special","spiritual","stunning","successful","superb","supporting",

            //T
            "terrific", "thoughtful","thrilling", "thriving", "touch", "trusting", "truthful", "try",

            //U
            "understand", "understood","unique", "unite", "touch", "unify", "unparalleled", "try",
            "uplift","upstanding","useful",

            //V
            "valued", "vibrant","victorious", "vigorous", "virtuous",

            //W
            "wealthy", "welcome","well", "whole", "willing", "wise", "wonderful", "worthy",

            //Y
            "yes", "yummy",

            //Z
            "zany", "zeal","zealous"
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_letter);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_word = (TextView) findViewById(R.id.tv_word);

        et_guess = (EditText) findViewById(R.id.et_guess);

        b_check = (Button) findViewById(R.id.b_check);
        b_new = (Button) findViewById(R.id.b_new);

        r = new Random();

        newGame();

        b_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_guess.getText().toString().equalsIgnoreCase(currentWord)){
                    tv_info.setText("Awesome!");
                    b_check.setEnabled(false);
                    b_new.setEnabled(true);
                }
                else{
                    tv_info.setText("Try Again!");
                }

            }
        });

        b_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

    }

    //shuffle algorithm
    public static String shuffledWord (String word){
        List<String> letters = Arrays.asList(word.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for(String letter : letters ){
            shuffled += letter;
        }
        return shuffled;
    }

    private void newGame(){
        //get random word from the dictionary
        currentWord = dictionary[r.nextInt(dictionary.length)];

        //show the shuffled word
        tv_word.setText(shuffledWord(currentWord));

        //clear the text field
        et_guess.setText("");

        //switch buttons
        b_new.setEnabled(false);
        b_check.setEnabled(true);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ArrangeLetterActivity.this,
                GamesActivity.class));
        finish();
    }
}