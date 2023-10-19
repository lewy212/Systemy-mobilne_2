package com.example.smlewy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_EXTRA_ANSWER = "com.example.smlewy.correctAnswer";

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("quiz","Wywołana metoda cyklu życia: onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("quiz","Wywołana metoda cyklu życia: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("quiz","Wywołana metoda cyklu życia: onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("quiz","Wywołana metoda cyklu życia: onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("quiz","Wywołana metoda cyklu życia: onResume");
    }

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private static final String KEY_CURRENT_INDEX = "currentIndex";

    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown;


    private int Wynik = 0;

    private Question[] questions = new Question[]{
            new Question(R.string.q_activity,true),
            new Question(R.string.q_version,false),
            new Question(R.string.q_find_resources,true),
            new Question(R.string.q_listener,true),
            new Question(R.string.q_resources,true),
            new Question(R.string.q_chrzest,true),
            new Question(R.string.q_messi,true),
            new Question(R.string.q_ronaldo,false)
    };
    private int currentIndex=0;
    private int restart = 0;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("quiz","Wywołana została metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX,currentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("quiz","Wywołana metoda cyklu życia: onCreate");

        if(savedInstanceState != null)
        {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        promptButton = findViewById(R.id.prompt_button);
        questionTextView = findViewById(R.id.question_text_view);



        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trueButton.setEnabled(false);
                falseButton.setEnabled(false);
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trueButton.setEnabled(false);
                falseButton.setEnabled(false);
                checkAnswerCorrectness(false);
            }
        });

        promptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER,correctAnswer);
                startActivityForResult(intent,REQUEST_CODE_PROMPT);

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerWasShown = false;
                if(currentIndex==questions.length-1)
                {
                    trueButton.setVisibility(View.GONE);
                    falseButton.setVisibility(View.GONE);
                    promptButton.setVisibility(View.GONE);
                    nextButton.setText("Restart");

                    questionTextView.setText("Twój wynik to: " + Integer.toString(Wynik) + "/"+ Integer.toString(questions.length));
                    restart = 1;
                    currentIndex = 0;
                }else {
                    if(restart != 1)
                    {
                        currentIndex = (currentIndex + 1)%questions.length;
                        setNextQuestion();
                    }
                   else
                    {
                        setNextQuestion();
                    }
                }

            }
        });

        setNextQuestion();
    }
    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrue();
        int resultMessageId = 0;
        if(answerWasShown)
        {
            resultMessageId = R.string.answer_was_shown;
        }else
        {
            if(userAnswer == correctAnswer)
            {
                resultMessageId = R.string.correct_answer;
                Wynik++;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }

        Toast.makeText(this,resultMessageId, Toast.LENGTH_SHORT).show();
    }
    private void setNextQuestion(){
        if(restart == 1)
        {
            trueButton.setVisibility(View.VISIBLE);
            falseButton.setVisibility(View.VISIBLE);
            promptButton.setVisibility(View.VISIBLE);
            nextButton.setText("Następne");
            restart = 0;
            Wynik = 0;
        }
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){return;}
        if(requestCode == REQUEST_CODE_PROMPT)
        {
            if(data == null) {return;}
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN,false);
        }
    }


}