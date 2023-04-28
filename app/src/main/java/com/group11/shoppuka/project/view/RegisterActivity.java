package com.group11.shoppuka.project.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Register");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F87217"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (binding.etPhoneNumber.getText().toString().isEmpty()){
                    binding.etPhoneNumber.setError("Số điện thoại không được bỏ trống");
                }
                else if (binding.etFullName.getText().toString().isEmpty()){
                    binding.etFullName.setError("Tên không được bỏ trống");
                }
                else if (binding.etPassword.getText().toString().isEmpty()){
                    binding.etPassword.setError("Password không được bỏ trống");
                }
                else if (binding.etConfirmPassword.getText().toString().isEmpty()){
                    binding.etConfirmPassword.setError("Xác Nhận Password không được bỏ trống");
                }
                else if (!binding.etPassword.getText().toString().matches(binding.etConfirmPassword.getText().toString())){
                    binding.etConfirmPassword.setError("Password không trùng khớp");
                }
                else {
                    Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_LONG);
                    finish();

                }
            }
        });
    }
}
