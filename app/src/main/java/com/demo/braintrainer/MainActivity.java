package com.demo.braintrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

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
    private int countOfQuestions = 0;
    private int countOfRightAnswer = 0;
    private boolean gameOver = false;


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
        playNext();

        //Создание таймера
        CountDownTimer timer = new CountDownTimer(100000,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTimer(millisUntilFinished));
                if(millisUntilFinished<10000){
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("00:00:000");
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = preferences.getInt("max",0);
                if(countOfRightAnswer >= max){
                    preferences.edit().putInt("max",countOfRightAnswer).apply();

                }
                //Создаем новую активность.
                Intent intent = new Intent(MainActivity.this,ScoreActivity.class);
                //Добавляем данные для активности
                intent.putExtra("result",countOfRightAnswer);
                //запускаем активность
                startActivity(intent);

            }
        };
        timer.start();
    }

    //Начинаем/продолжаем играть
    private void playNext(){
        generateQuestions(); //Генерация данных(вопросов и ответов)
        for (int i = 0; i<options.size(); i++){
            if(i == rightAnswerPosition){
                options.get(i).setText(Integer.toString(rightAnswer));
            }
            else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s / %s", countOfRightAnswer,countOfQuestions);
        textViewScore.setText(score);
    }

    //Генерация вопроса
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
        //присваеваем полю сгенерированый вопросс
        textViewQuestion.setText(question);

        //Генерируем позицию для правильного ответа
        rightAnswerPosition = (int) (Math.random()*4);

    }

    //Генерация не правильных результатов
    private int generateWrongAnswer(){
        int result;
        do{
            result = (int) (Math.random() * max * 2 + 1)-(max-min);
        }while (result == rightAnswer);
        return result;
    }

    //Форматируем время
    private String getTimer(long millis){
        int seconds = (int) millis/1000;
        int minutes = seconds/60;

        seconds = seconds % 60;
        int mill = (int) (millis % 1000);
        return String.format(Locale.getDefault(),"%02d:%02d:%03d", minutes,seconds,mill);
    }

    //Реакция на нажатие кнопки
    public void onClickAnswer(View view) {
        if(!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answer);

            //Проверяем правильность выбора
            if (chosenAnswer == rightAnswer) {
                Toast.makeText(this, "Это правильно!", Toast.LENGTH_SHORT).show();
                countOfRightAnswer++;
            } else {
                Toast.makeText(this, "Не правильно!", Toast.LENGTH_SHORT).show();
            }
            countOfQuestions++;
            playNext();
        }
    }
}
