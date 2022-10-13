package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.example.quizapp.Adapters.AnswerAdapter;
import com.example.quizapp.Models.QAs.QuestionAnswers;
import com.example.quizapp.R;
import com.example.quizapp.Retrofit.RetrofitClient;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizPage extends AppCompatActivity {


    int scoreCheck = 0;
    int wrongCheck=0;
    int unattempted=0;

    int increment;
    int time;
    int end;
    int itemSize;
    ArrayList<QuestionAnswers> items = new ArrayList<>();

    CountDownTimer countDownTimer;
    TextView timertext;
    LinearProgressIndicator timer;




    TextView question;
    RecyclerView recyclerView;
    LazyLoader loader;
    LinearLayout buttonLayout;
    Button submitButton;


    RecyclerView.OnItemTouchListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));

        fetchPosts();


        increment=0;

        question=findViewById(R.id.questionText);
        loader=findViewById(R.id.lazyloader);
        recyclerView = findViewById(R.id.answerView);
        buttonLayout=findViewById(R.id.buttonlayout);
        submitButton=findViewById(R.id.submitButton);
        timer=findViewById(R.id.timer);
        timertext=findViewById(R.id.timertext);



    }

    public void incrementButton(View v){
        if(items.get(increment).getCorrectChoice()==2 || items.get(increment).getCorrectChoice()==3){
            if(items.get(increment).getCorrectChoice()==2){
                wrongCheck+=1;
            }
            if(increment<itemSize){
                increment+=1;
                initiateChange(increment);
            }
        }
        else if(items.get(increment).getCorrectChoice()==1){
            scoreCheck+=1;
            if(increment<itemSize){
                increment+=1;
                initiateChange(increment);
            }
        }
        else {
            Toast.makeText(QuizPage.this,"Please Choose Your Choice!",Toast.LENGTH_SHORT).show();
        }

        if (increment==itemSize){
            v.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        }
    }


    public void submitButton(View v){

        try{
            countDownTimer.cancel();
        }catch (Exception ex){
        }

        if(items.get(increment).getCorrectChoice()==1){
            scoreCheck+=1;

        }

        if(items.get(increment).getCorrectChoice()==2){
            wrongCheck+=1;
        }

        if(items.get(increment).getCorrectChoice()==0){
            Toast.makeText(QuizPage.this,"All Questions are Compulsory",Toast.LENGTH_SHORT).show();
            return;
        }


        end=1;


        Intent intent = new Intent(this, scorePage.class);
        intent.putExtra("Activity","2");
        intent.putExtra("Score",Integer.toString(scoreCheck));
        intent.putExtra("wrong",Integer.toString(wrongCheck));
        intent.putExtra("unattempted",Integer.toString(unattempted));
        intent.putExtra("Size",Integer.toString(itemSize+1));

        //implementing first Mock
        SharedPreferences sh = getApplicationContext().getSharedPreferences("userSharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        int s1 = sh.getInt("firstTime", 0);
        if(s1==1){
            editor.putInt("notFirstTime", 1);
            editor.commit();
        }

        startActivity(intent);
        finish();
    }

    public void initiateChange(int increment){
        try{
            countDownTimer.cancel();
            recyclerView.removeOnItemTouchListener(listener);
        }catch (Exception ex){
        }

        question.setText((increment + 1)+". "+items.get(increment).getQuestion());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AnswerAdapter answeradapter=new AnswerAdapter(this, items.get(increment).getAnswers(),items.get(increment));
        recyclerView.setAdapter(answeradapter);
        recyclerView.startLayoutAnimation();


        int TIME=20000;
        time=20;
        countDownTimer=new CountDownTimer(TIME,1000){
            @Override
            public void onTick(long l) {
                timertext.setText(time+"s");
                time=time-1;
                float percentage = (float) (((float) time / ((float) TIME/1000.0))*100.0);
                int finalScoreValueText = Math.round(percentage);
                timer.setProgressCompat(finalScoreValueText,true);
            }

            @Override
            public void onFinish() {
                //Display Time UP

                timertext.setText("TIMES UP!");

                if(items.get(increment).getCorrectChoice()==0){
                    items.get(increment).setCorrectChoice(3);
                    unattempted+=1;
                }


                listener=(new RecyclerView.SimpleOnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        Toast.makeText(QuizPage.this,"TIMES UP!",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                recyclerView.addOnItemTouchListener(listener);


            }
        }.start();

    }



    public void fetchPosts(){
        RetrofitClient.getRetrofitClient().getQuestions().enqueue(new Callback<List<QuestionAnswers>>() {
            @Override
            public void onResponse(Call<List<QuestionAnswers>> call, Response<List<QuestionAnswers>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    items.addAll(response.body());
                    loader.setVisibility(View.GONE);
                    buttonLayout.setVisibility(View.VISIBLE);
                    itemSize=items.size()-1;
                    initiateChange(increment);

                }
            }

            @Override
            public void onFailure(Call<List<QuestionAnswers>> call, Throwable t) {
                Toast.makeText(QuizPage.this,"Network Unavailable!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    @Override
    public void onBackPressed(){
        timer.setVisibility(View.GONE);
        if(countDownTimer!=null){countDownTimer.cancel();}
        Toast.makeText(QuizPage.this,"Backed!",Toast.LENGTH_SHORT).show();
        finishAfterTransition();
    }
}