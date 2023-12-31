package com.example.lab11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab11.Database.Database;
import com.example.lab11.Database.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "Score More";
    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyscore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String Key_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";


    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;

    private Drawable backGroundColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private  long timeLeftInMillis;

    private int currentLevel;

    private int playSound;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;
    int minutes,seconds;
    MediaPlayer player1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id._text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        player1= MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        //add a drawable src media and load
        //in your assignment as the player media
        backGroundColorDefaultRb = rb1.getBackground();
        textColorDefaultCd = textViewCountDown.getTextColors();

        if (savedInstanceState == null){

            Database dbHelper = new Database(this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestions();

        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(Key_ANSWERED);

            if (!answered){
                startCountDown();
            }else {
                updateCountDownText();
                showSolution();
            }
        }
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();

                    } else {
                        Toast.makeText(QuizActivity.this, "Select an answer", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showNextQuestions();
                    System.out.println("Hi2222222222222222222222222222");

                        if(player1!=null)
                        {
                            player1.stop();
                            player1.prepareAsync();
                        }

                }
            }
        });
    }

    private void showNextQuestions() {
        rb1.setBackgroundColor(Color.rgb(255,127,80));
        rb2.setBackgroundColor(Color.rgb(255,127,80));
        rb3.setBackgroundColor(Color.rgb(255,127,80));
        rbGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/10" );
            answered = false;
            buttonConfirmNext.setText("Apply it");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;


                    startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown(){

        if (currentLevel == 1) {
            timeLeftInMillis = 200000;
        } else {
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                showNextQuestions();
            }
        }.start();
    }

    private  void updateCountDownText(){
         minutes =  (int )(timeLeftInMillis / 1000) / 60;
         seconds = (int) (timeLeftInMillis /1000) %60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);

        textViewCountDown.setText(timeFormatted);

        if( timeLeftInMillis < 20000 && timeLeftInMillis > 10000){
            textViewCountDown.setTextColor(Color.rgb(255,255,0));
        }else if (timeLeftInMillis < 10000){
            textViewCountDown.setTextColor(Color.RED);
        }
        else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
            answered = true;

            countDownTimer.cancel();

            RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
            int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

            if (answerNr == currentQuestion.getAnswerNr()) {
                playSound(R.raw.correct_sound); // Play correct sound
                score++;
                textViewScore.setText("Score: " + score);
            } else {
                playSound(R.raw.wrong_sound); // Play wrong sound
            }

            showSolution();
        }
    private void playSound(int soundResourceId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResourceId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }


    private void showSolution(){
        rb1.setBackgroundColor(Color.RED);
        rb2.setBackgroundColor(Color.RED);
        rb3.setBackgroundColor(Color.RED);

        switch (currentQuestion.getAnswerNr()){

            case 1:
                rb1.setBackgroundColor(Color.GREEN);

                textViewQuestion.setText("Answer 1  Correct");


                break;

            case 2:
                rb2.setBackgroundColor(Color.GREEN);

                textViewQuestion.setText("Answer 2  Correct");

                break;

            case 3:
                rb3.setBackgroundColor(Color.GREEN);

                textViewQuestion.setText("Answer 3  Correct");

                break;
        }

        if(questionCounter < questionCountTotal){
            buttonConfirmNext.setText("Next");


        }else{
            buttonConfirmNext.setText("Confirm");

        }




    }
    private void finishQuiz () {
        Intent resultIntent= new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime +2000 > System.currentTimeMillis()){
            finishQuiz();
        }
        else {
            Toast.makeText(this, " Press again", Toast.LENGTH_SHORT).show();
        }
        backPressedTime= System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer !=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(Key_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);

    }

public  void play(){
        while(true){
            player1.start();
        }
}
}