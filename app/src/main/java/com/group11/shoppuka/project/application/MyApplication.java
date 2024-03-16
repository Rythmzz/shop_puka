package com.group11.shoppuka.project.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.TextView;

import java.text.DecimalFormat;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {


    public static String localHost = "http://192.168.158.1:1337";
    public static final int PICK_IMAGE = 1;

    public static String KEY_GET_LISTCART = "list_cart";
    public static String KEY_ACCOUNT_PHONE = "phone_info";

    public static String KEY_LOGIN = "login_info";

    public static String FULL_NAME_PHONE = "fullName_phone_info";

    public static String AVATAR_ACCOUNT = "avatar_account_info";

    public static String ID_MODE = "id_mode_info";

    public static String ID_ACCOUNT = "id_account_info";

    public static String STATUS_LOGIN = "status_login";
    
    @SuppressLint("SetTextI18n")
    public static void setCategorySpecific(int idCategory, TextView text){
        switch (idCategory){
            case 0: {
                text.setText("Loại: Máy Tính");
                break;
            }
            case 1: {
                text.setText("Loại: Phần Cứng");
                break;
            }
            case 2: {
                text.setText("Loại: Laptop");
                break;
            }
            case 3: {
                text.setText("Loại: Tai Nghe");
                break;
            }
            case 4: {
                text.setText("Loại: Điện Thoại");
                break;
            }
            case 5: {
                text.setText("Loại: Bàn Phím");
                break;
            }
            case 6: {
                text.setText("Loại: Màn Hình");
                break;
            }
            case 7: {
                text.setText("Loại: Bàn Ghế");
                break;
            }
            case 8: {
                text.setText("Loại: Tay Cầm");
                break;
            }
            case 9: {
                text.setText("Loại: Bàn Di Chuột");
                break;
            }
            case 10: {
                text.setText("Loại: Công Cụ Livestream");
                break;
            }
            case 11: {
                text.setText("Loại: Chuột");
                break;
            }

        }
    }

    public static String formatCurrency(String input) {
        try {
            long number = Long.parseLong(input);
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(number);
        } catch (NumberFormatException e) {

            return input;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
