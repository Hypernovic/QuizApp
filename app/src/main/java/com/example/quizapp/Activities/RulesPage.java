package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quizapp.R;

public class RulesPage extends AppCompatActivity {

    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_page);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));

        checkBox1=findViewById(R.id.checkbox1);
        checkBox2=findViewById(R.id.checkbox2);
        checkBox3=findViewById(R.id.checkbox3);
        checkBox4=findViewById(R.id.checkbox4);
        checkBox5=findViewById(R.id.checkbox5);
        checkBox6=findViewById(R.id.checkbox6);


    }


    public void next(View view) {
        if(allChecked()){
            change();
        }
    }

    private void change() {
        Intent Intent= new Intent(RulesPage.this, MainQuizPage.class);
        Pair[] pair= new Pair[1];
        LinearLayout layout=findViewById(R.id.background_layout);
        pair[0]=new Pair<View,String>(layout,"layout");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(RulesPage.this, pair);
        ActivityCompat.startActivity(RulesPage.this,Intent,options.toBundle());
    }

    public boolean allChecked(){
        if (checkBox1.isChecked()
                && checkBox2.isChecked()
                && checkBox3.isChecked()
                && checkBox4.isChecked()
                && checkBox5.isChecked()
                && checkBox6.isChecked()){
            return true;
        }
        Toast.makeText(RulesPage.this,"Check all to Continue!",Toast.LENGTH_SHORT).show();
        return false;
    };

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAfterTransition();
    }



}