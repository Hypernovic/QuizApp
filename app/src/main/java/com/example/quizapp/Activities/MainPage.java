package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.Retrofit.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {

    LinearLayout layout;

    boolean doubleBackToExitPressedOnce = false;

    FirebaseAuth mAuth;

    Dialog dialog;
    Button finalSubmit;

    Integer attemptValue;

    DrawerLayout drawerLayout;

    String studentName;
    String studentSchool;
    String studentCity;
    Integer studentGrade;

    TextView studentNameView;
    TextView studentSchoolView;
    TextView studentCityView;
    TextView studentGradeView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        layout=findViewById(R.id.background_layout);
        drawerLayout=findViewById(R.id.drawer_layout);

        SharedPreferences sh = getSharedPreferences("userSharedPref",MODE_PRIVATE);
        studentName = sh.getString("userName", null);
        studentSchool = sh.getString("userSchool", null);
        studentCity = sh.getString("userCity", null);
        studentGrade = sh.getInt("userGrade", 0);

        studentNameView = findViewById(R.id.studentNameView);
        studentSchoolView = findViewById(R.id.studentSchoolView);
        studentCityView = findViewById(R.id.studentCityView);
        studentGradeView = findViewById(R.id.studentGradeView);

        studentNameView.setText("Name: "+studentName);
        studentSchoolView.setText("School: "+studentSchool);
        studentCityView.setText("City: "+studentCity);
        studentGradeView.setText("Grade: "+studentGrade.toString());






        //implement first Mock shared Preferences
        mAuth = FirebaseAuth.getInstance();


    }

    private boolean isFirstTime() {
        SharedPreferences sh = getApplicationContext().getSharedPreferences("userSharedPref", Context.MODE_PRIVATE);
        int s1 = sh.getInt("notFirstTime",10);
        if(s1==1){
            return false;
        }
        else{
            return true;
        }

    }

    public void mockTest(View v){
        Intent Intent= new Intent(MainPage.this, QuizPage.class);
        Pair[] pair= new Pair[1];
        pair[0]=new Pair<View,String>(layout,"layout");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainPage.this, pair);
        ActivityCompat.startActivity(MainPage.this,Intent,options.toBundle());
    }

    public void mainQuiz(View v){
        if(isFirstTime()){
            Toast.makeText(MainPage.this,"Attempt a Mock Quiz First!",Toast.LENGTH_SHORT).show();
            return;
        }
        //Check for attempts
        if(attmeptsOver()){
            return;
        }
        else{
            showAttemptsDialog();
        }

    }

    public boolean attmeptsOver() {
        FirebaseUser currentUser = this.mAuth.getCurrentUser();

        Call<JsonObject> call = RetrofitClient.getRetrofitClient().checkAttempts(currentUser.getUid());

        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code()==200){
                    JsonObject object=response.body();
                    JsonElement attempts=object.get("attempts");
                    attemptValue=Integer.parseInt(attempts.getAsString());
                }

                if(response.code()==400){
                    showDeletedDialog();
                    Toast.makeText(MainPage.this,"Something Fishy",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if(attemptValue==null){
                    Toast.makeText(MainPage.this,"Network Unavailable!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(attemptValue==null){
            Toast.makeText(MainPage.this,"Please Wait for a Moment!",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(attemptValue<2){
            return false;
        }else {
            Toast.makeText(MainPage.this,"You have used your given two attempts!",Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    @Override
    public void onBackPressed(){
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainPage.this,"Back Again to Close",Toast.LENGTH_SHORT).show();
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

    private void showAttemptsDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.attempts_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialog.show();

        finalSubmit = dialog.findViewById(R.id.btn_okay);
        TextView textView = dialog.findViewById(R.id.textView);

        textView.setText(setAttemptDialogText());

        finalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent Intent= new Intent(MainPage.this, RulesPage.class);
                Pair[] pair= new Pair[1];
                pair[0]=new Pair<View,String>(layout,"layout");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainPage.this, pair);
                ActivityCompat.startActivity(MainPage.this,Intent,options.toBundle());
            }
        });
    }

    private String setAttemptDialogText() {
        if(attemptValue==0){
            return "The following attempt is your First\nYou have one attempt remaining";
        }
        if(attemptValue==1){
            return "The following attempt is your Second\nThis is Your Final attempt";
        }
        return "Something's Fishy";
    };

    public void drawerOpen(View view) {

        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void subscribe(View view) {
        Toast.makeText(MainPage.this,"Under Process!",Toast.LENGTH_SHORT).show();
    }
}