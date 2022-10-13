package com.example.quizapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.util.Pair;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.quizapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.skyfishjy.library.RippleBackground;

public class MainActivity extends AppCompatActivity {

    ImageView logo;

    LinearLayout layout;

    Dialog dialog;

    private FirebaseAuth mAuth;




    public static int SPLASH_SCREEN= 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        logo=findViewById(R.id.logo_image);
        layout=findViewById(R.id.background_layout);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        mAuth = FirebaseAuth.getInstance();


        new Handler().postDelayed(() -> {

            if(!isConnected(this)){
                showInternetDialog();
            }

            else if(!isFirebaseAuth(mAuth) && isStored()){
                showDeletedDialog();
            }

            else if(!isFirebaseAuth(mAuth)){
                Intent Intent= new Intent(MainActivity.this, OtpSendActivity.class);
                startActivity(Intent);
                finish();
            }

            else if(!isStored()){
                Intent Intent= new Intent(MainActivity.this, RegistrationPage.class);
                Pair[] pair= new Pair[1];
                pair[0]=new Pair<View,String>(logo,"logo_image");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair);
                ActivityCompat.startActivity(MainActivity.this,Intent,options.toBundle());
            }

            else{
            Intent Intent= new Intent(MainActivity.this, MainPage.class);
            Pair[] pair= new Pair[1];
            pair[0]=new Pair<View,String>(logo,"logo_image");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair);
            ActivityCompat.startActivity(MainActivity.this,Intent,options.toBundle());

            }
        },SPLASH_SCREEN);
    }

    private void showDeletedDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.deleted_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialog.show();
    }

    private boolean isStored() {
        try{
        SharedPreferences sh = getSharedPreferences("userSharedPref",MODE_PRIVATE);
        String s1 = sh.getString("firebaseUID", null);
//        Toast.makeText(MainActivity.this, s1, Toast.LENGTH_SHORT).show();
            if(s1 != null){
            return true;
        } else{
            return false;
        }}catch (Exception e){
            return false;
        }
    }

    private boolean isFirebaseAuth(FirebaseAuth mAuth) {
        try{
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return false;
        }
        return true;
        }catch (Exception e){
            return false;
        }
    }



    //Internet Functions
    private void showInternetDialog() {
        //Creating Dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.nointernet_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button connect = dialog.findViewById(R.id.btn_connect);

        dialog.show();

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(MainActivity.this)){
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
                else{
                    recreate();
                }
            }
        });

    }

    private boolean isConnected(MainActivity mainActivity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn !=null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            return true;
        }
        else{
            return false;
        }


    }


}