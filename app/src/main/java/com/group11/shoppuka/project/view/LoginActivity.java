package com.group11.shoppuka.project.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityLoginBinding;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                confirmOtp();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });


    }


    private void confirmOtp() throws JSONException {
        //Tạo một LayoutInflater object cho hộp thoại
        LayoutInflater li = LayoutInflater.from(this);
        //Tạo một view để lấy hộp thoại
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Khởi tạo nút xác nhận cho hộp thoại và EditText của hộp thoại
        Button buttonConfirm = (Button) confirmDialog.findViewById(R.id.buttonConfirm);
//        EditText editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);
        Button buttonResend = (Button) confirmDialog.findViewById(R.id.buttonResend);

        //Tạo một AlertDialog.Builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Thêm hộp thoại của chúng ta vào view của AlertDialog
        alert.setView(confirmDialog);

        //Tạo một AlertDialog
        final AlertDialog alertDialog = alert.create();

        //Hiển thị hộp thoại cảnh báo
        alertDialog.show();

        //Khi nhấn nút xác nhận từ hộp thoại cảnh báo
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy mã OTP do người dùng nhập từ EditText
//                final String otp = editTextConfirmOtp.getText().toString().trim();


//                System.out.println(otp.toString());

                //Kiểm tra mã OTP
                //...
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
                        buttonResend.setEnabled(true);
                        buttonResend.setText("Resend OTP");
                    }
                }.start();
            }
        });
    }
}
