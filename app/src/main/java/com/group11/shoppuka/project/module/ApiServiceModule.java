package com.group11.shoppuka.project.module;

import com.group11.shoppuka.project.service.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class ApiServiceModule {
    @Singleton
    @Provides
    public static ApiService providedApiService(Retrofit.Builder retrofit){
        return retrofit.build().create(ApiService.class);
    }
}
