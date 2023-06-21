package com.group11.shoppuka.project.module;

import com.group11.shoppuka.project.application.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {
    @Singleton
    @Provides
    public static Retrofit.Builder providedRetrofit(){
        return new Retrofit.Builder().baseUrl(MyApplication.localHost + "/")
                .addConverterFactory(GsonConverterFactory.create());
    }
}
