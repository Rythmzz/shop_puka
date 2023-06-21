package com.group11.shoppuka.project.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.group11.shoppuka.project.application.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SharedPreferenceModule {
    @Singleton
    @Provides
    public static SharedPreferences providedSharedPreference(@ApplicationContext Context context){
        return context.getSharedPreferences(MyApplication.KEY_LOGIN,Context.MODE_PRIVATE);
    }
}
