package com.group11.shoppuka.project.other;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {



    public static String localHost = "http://192.168.1.4:1337";
    public static final int PICK_IMAGE = 1;

    public static String KEY_GET_LISTCART = "list_cart";
    public static String KEY_ACCOUNT_PHONE = "phone_info";

    public static String KEY_LOGIN = "login_info";

    public static String FULL_NAME_PHONE = "fullName_phone_info";

    public static String AVATAR_ACCOUNT = "avatar_account_info";

    public static String ID_MODE = "id_mode_info";

    public static String ID_ACCOUNT = "id_account_info";

    public static String STATUS_LOGIN = "status_login";
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
