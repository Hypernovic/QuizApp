package com.example.quizapp.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL="https://finalquizapp.herokuapp.com//";
    private static Retrofit retrofit = null;

    public static ApiInterface getRetrofitClient(){


        if(retrofit==null) {
        synchronized (ApiInterface.class){
            //for Multi Threaded Lazy initialisation of Singleton
            if (retrofit==null){ //Early Loading Singleton (Applicable for Single-Threaded Not Multi Threaded)
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        } }



        return retrofit.create(ApiInterface.class);
    }

}
