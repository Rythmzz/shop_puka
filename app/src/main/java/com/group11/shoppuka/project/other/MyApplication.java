package com.group11.shoppuka.project.other;

import android.app.Application;

public class MyApplication extends Application {
    public static String localHost = "http://192.168.158.1:1337";
    public static final int PICK_IMAGE = 1;

    public static String KEY_GET_LISTCART = "list_cart";
    public static String KEY_ACCOUNT_PHONE = "phone_info";

    public static String KEY_LOGIN = "login_info";
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
