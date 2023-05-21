package com.group11.shoppuka.project.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityLoginBinding;

import org.json.JSONException;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static String LOGIN_KEY = "login_info";
    private static String ACCOUNT_PHONE = "phone_info";
    private static String PASSWORD_PHONE = "password_phone_info";
    private void saveLoginInfo(Context context, String phoneNumber, String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCOUNT_PHONE, phoneNumber);
        editor.putString(PASSWORD_PHONE,password);
        editor.apply();
    }

    private String[] getLoginInfo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_KEY,Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(ACCOUNT_PHONE,null);
        String password = sharedPreferences.getString(PASSWORD_PHONE,null);
        return new String[]{phoneNumber,password};
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] loginInfo = getLoginInfo(this);
        System.out.println("LUU DU LIEU " + loginInfo[0]);
        System.out.println("LUU DU LIEU " +loginInfo[1]);
        if (loginInfo[0] != null && loginInfo[1] != null){
            Intent intent =new Intent(getApplicationContext(),MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        getSupportActionBar().setTitle("Login");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F87217"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tvRegister.setOnClickListener( view1 ->{
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(view1 -> {
            try {
                if (binding.etAccount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Vui lòng không để trống số điện thoại!",Toast.LENGTH_LONG);
                    binding.etAccount.setError("Vui lòng không để trống số điện thoại!");
                }
                else if (binding.etPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Vui lòng không để trống mật khẩu!",Toast.LENGTH_LONG);
                    binding.etPassword.setError("Vui lòng không để trống mật khẩu!");
                }
                else {
                    if (String.valueOf(binding.etAccount.getText()).equals("123456")
                            && String.valueOf(binding.etPassword.getText()).equals("123456")){
                        confirmOtp(this);
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Đăng nhập thất bại! Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });


    }


    private void confirmOtp(Context context) throws JSONException {
        //Tạo một LayoutInflater object cho hộp thoại
        LayoutInflater li = LayoutInflater.from(this);
        //Tạo một view để lấy hộp thoại
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Khởi tạo nút xác nhận cho hộp thoại và EditText của hộp thoại
        Button buttonConfirm = (Button) confirmDialog.findViewById(R.id.buttonConfirm);
//        EditText editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
        Button buttonResend = (Button) confirmDialog.findViewById(R.id.buttonResend);

        EditText otpNumber1 = (EditText) confirmDialog.findViewById(R.id.otpNumber1);
        EditText otpNumber2 = (EditText) confirmDialog.findViewById(R.id.otpNumber2);
        EditText otpNumber3 = (EditText) confirmDialog.findViewById(R.id.otpNumber3);
        EditText otpNumber4 = (EditText) confirmDialog.findViewById(R.id.otpNumber4);
        EditText otpNumber5 = (EditText) confirmDialog.findViewById(R.id.otpNumber5);
        EditText otpNumber6 = (EditText) confirmDialog.findViewById(R.id.otpNumber6);

        //Tạo một AlertDialog.Builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Thêm hộp thoại của chúng ta vào view của AlertDialog
        alert.setView(confirmDialog);

        //Tạo một AlertDialog
        final AlertDialog alertDialog = alert.create();

        //Hiển thị hộp thoại cảnh báo
        alertDialog.show();

        otpNumber1.setText("2");
        otpNumber2.setText("5");
        otpNumber3.setText("1");
        otpNumber4.setText("2");
        otpNumber5.setText("0");
        otpNumber6.setText("1");

        //Khi nhấn nút xác nhận từ hộp thoại cảnh báo
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy mã OTP do người dùng nhập từ EditText
//                final String otp = editTextConfirmOtp.getText().toString().trim();
                if (binding.cbRemember.isEnabled()){
                    saveLoginInfo(context,binding.etAccount.getText().toString(),binding.etPassword.getText().toString());
                }
                alertDialog.dismiss();
                Intent intent =new Intent(getApplicationContext(),MainPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Vô hiệu hóa nút Resend
                buttonResend.setEnabled(false);

                //Tạo một bộ đếm thời gian với thời gian là 30 giây
                new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        //Cập nhật text của nút Resend trong khi đếm ngược
                        buttonResend.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds");
                    }

                    public void onFinish() {
                        //Khi bộ đếm thời gian kết thúc, cho phép người dùng nhấn nút Resend và cập nhật text của nút
                        Random rand = new Random();
                        buttonResend.setEnabled(true);
                        buttonResend.setText("Resend OTP");
                        otpNumber1.setText(Integer.toString((rand.nextInt(10))));
                        otpNumber2.setText(Integer.toString((rand.nextInt(10))));
                        otpNumber3.setText(Integer.toString((rand.nextInt(10))));
                        otpNumber4.setText(Integer.toString((rand.nextInt(10))));
                        otpNumber5.setText(Integer.toString((rand.nextInt(10))));
                        otpNumber6.setText(Integer.toString((rand.nextInt(10))));
                    }
                }.start();
            }
        });
    }
}
