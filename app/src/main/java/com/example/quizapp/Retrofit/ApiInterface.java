package com.example.quizapp.Retrofit;

import com.example.quizapp.Models.QAs.QuestionAnswers;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("/getQuestions")
    Call<List<QuestionAnswers>> getQuestions();

    @POST("/newUser")
    @FormUrlEncoded
    Call<Void> registerUser(@Field("userName") String userName, @Field("userSchool") String school,@Field("userCity") String city,@Field("firebaseUID") String firebaseUID,@Field("Grade") Integer grade);

    @POST("/userRemarks")
    @FormUrlEncoded
    Call<Void> userRemarks(@Field("firebaseUID") String firebaseUID,@Field("Score") Integer score);

    @POST("/checkAttempts")
    @FormUrlEncoded
    Call<JsonObject> checkAttempts(@Field("firebaseUID") String firebaseUID);



}
