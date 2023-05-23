package com.group11.shoppuka.project.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
     public Retrofit retrofit;

     public RetrofitService(){
         retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.106:1337/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
     }
}
