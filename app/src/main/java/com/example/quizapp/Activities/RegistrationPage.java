package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.Retrofit.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shawnlin.numberpicker.NumberPicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationPage extends AppCompatActivity {


    FirebaseAuth mAuth;

    private Dialog dialog;
    private Button submit;
    NumberPicker grade;

    ProgressBar progressBar;
    Button finalSubmit;

    com.google.android.material.textfield.TextInputEditText userName;
    com.google.android.material.textfield.TextInputEditText userSchool;
    com.google.android.material.textfield.TextInputEditText userCity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        postponeEnterTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        mAuth = FirebaseAuth.getInstance();

        submit = findViewById(R.id.btn_submit);


        userName= findViewById(R.id.studentName);
        userSchool=findViewById(R.id.schoolName);
        userCity=findViewById(R.id.studentCity);



        //Creating Dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.grade_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        progressBar = dialog.findViewById(R.id.progressBar);
        finalSubmit = dialog.findViewById(R.id.btn_okay);
        grade =dialog.findViewById(R.id.number_picker);


        startPostponedEnterTransition();


        finalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().trim().isEmpty() ||
                        userCity.getText().toString().trim().isEmpty() ||
                        userSchool.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegistrationPage.this, "Please fill the required Information!", Toast.LENGTH_SHORT).show();
                }
                else {
                    doAuthenticate();
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(); // Showing the dialog here
            }
        });

    }

    private void doAuthenticate() {
        finalSubmit.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        FirebaseUser currentUser = this.mAuth.getCurrentUser();

        Call<Void> call = RetrofitClient.getRetrofitClient().registerUser(
                userName.getText().toString().trim(),
                userSchool.getText().toString().trim(),
                userCity.getText().toString().trim(),
                currentUser.getUid(),
                grade.getValue());



        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {



                if(response.code()==201){
                    Toast.makeText(RegistrationPage.this,"Registration Successful",Toast.LENGTH_LONG).show();

                    //Do shared Preferences here
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userSharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor userEdit = sharedPreferences.edit();
                    userEdit.putString("userName", userName.getText().toString().trim());
                    userEdit.putString("userSchool", userSchool.getText().toString().trim());
                    userEdit.putString("userCity", userCity.getText().toString().trim());
                    userEdit.putString("firebaseUID", currentUser.getUid());
                    userEdit.putInt("userGrade", grade.getValue());

                    userEdit.putInt("firstTime", 1);

                    userEdit.commit();

                    dialog.dismiss();

                    Intent restart= new Intent(RegistrationPage.this, MainActivity.class);
                    startActivity(restart);
                    finishAffinity();
                }

                if(response.code()==401){
                    Toast.makeText(RegistrationPage.this,"Something Fishy",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegistrationPage.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                submit.setVisibility(View.VISIBLE);
            }
        });

    }

    public void onBackPressed(){
        Toast.makeText(RegistrationPage.this,"Register To Continue",Toast.LENGTH_SHORT).show();
    }

}
