package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.Retrofit.RetrofitClient;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class scorePage extends AppCompatActivity {

    CircularProgressIndicator Score;
    TextView finalscoreText;
    TextView correctText;
    TextView wrongText;
    TextView unattemptedText;

    Dialog dialog;

    ProgressBar progressBar;

    int scoreValue;

    String checkActivity;

    int checkActivityValue;

    FirebaseAuth mAuth;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));



        Intent intent = getIntent();
        checkActivity = intent.getStringExtra("Activity");
        String scoreText = intent.getStringExtra("Score");
        String wrong = intent.getStringExtra("wrong");
        String unattempted = intent.getStringExtra("unattempted");
        String sizeText = intent.getStringExtra("Size");


        scoreValue = Integer.parseInt(scoreText);

        checkActivityValue=Integer.parseInt(checkActivity);

        int sizeValue = Integer.parseInt(sizeText);
        float percentage = (float) (((float) scoreValue / (float) sizeValue )*100.0);
        int finalScoreValueText = Math.round(percentage);



        Score=findViewById(R.id.scoreIndicator);
        finalscoreText=findViewById(R.id.scoreText);
        wrongText=findViewById(R.id.wrongTextView);
        unattemptedText=findViewById(R.id.unattemtedTextView);
        correctText=findViewById(R.id.correctTextView);

        progressBar=findViewById(R.id.progressBar);

        finalscoreText.setText(scoreText+"/"+sizeText);


        correctText.setText(scoreText);
        wrongText.setText(wrong);
        unattemptedText.setText(unattempted);

        Score.setProgressCompat(finalScoreValueText,true);

        if(checkActivityValue==1){
            mAuth = FirebaseAuth.getInstance();
            if(postData()){
                progressBar.setVisibility(View.GONE);
            }
        }
        else{
            progressBar.setVisibility(View.GONE);
        }

    }

    private boolean postData() {
        FirebaseUser currentUser = this.mAuth.getCurrentUser();

        Call<Void> call = RetrofitClient.getRetrofitClient().userRemarks(
                currentUser.getUid(),
                scoreValue);



        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    Toast.makeText(scorePage.this,"Remarks Successfully Sent",Toast.LENGTH_LONG).show();
                }

                if(response.code()==400){
                    showDeletedDialog();
                    Toast.makeText(scorePage.this,"Something Fishy",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(scorePage.this,"Network Unavailable!",Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }


    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(scorePage.this,"Back Again to Close",Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
            }
            , 2000);
    }

    private void showDeletedDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.deleted_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialog.show();
    }

}