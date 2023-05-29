package com.group11.shoppuka.project.service;

import com.group11.shoppuka.project.other.MyApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
     public Retrofit retrofit;

     public RetrofitService(){
         retrofit = new Retrofit.Builder().baseUrl(MyApplication.localHost + "/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
     }
}
