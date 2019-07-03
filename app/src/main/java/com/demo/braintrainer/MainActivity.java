package com.demo.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewQuestion;
    private TextView textViewOpinion0;
    private TextView textViewOpinion1;
    private TextView textViewOpinion2;
    private TextView textViewOpinion3;
    private ArrayList<TextView> options = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max =30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewOpinion0 = findViewById(R.id.textViewOpinion0);
        textViewOpinion1 = findViewById(R.id.textViewOpinion1);
        textViewOpinion2 = findViewById(R.id.textViewOpinion2);
        textViewOpinion3 = findViewById(R.id.textViewOpinion3);
        options.add(textViewOpinion0);
        options.add(textViewOpinion1);
        options.add(textViewOpinion2);
        options.add(textViewOpinion3);

        generateQuestions();
        for (int i = 0; i<options.size(); i++){
            if(i == rightAnswerPosition){
                options.get(i).setText(Integer.toString(rightAnswer));
            }
            else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }

    }

    private void generateQuestions(){
        int a = (int)(Math.random() * (max-min+1) + min);
        int b = (int)(Math.random() * (max-min+1) + min);
        int mark = (int)(Math.random() * 2);
        isPositive = mark == 1;

        if(isPositive){
            rightAnswer = a + b;
            question = String.format("%s + %s", a,b);
        }
        else{
            rightAnswer = a - b;
            question = String.format("%s - %s", a,b);
        }
        rightAnswerPosition = (int) Math.random()*4;
        //присваеваем полю сгенерированый вопросс
        textViewQuestion.setText(question);
    }

    private int generateWrongAnswer(){
        int result;
        do{
            result = (int) (Math.random() * max * 2 + 1)-(max-min);
        }while (result == rightAnswer);
        return result;
    }
}
