package com.group11.shoppuka.project.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityRegisterBinding;
import com.group11.shoppuka.project.model.account.UserData;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.viewmodel.LoginViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private LoginViewModel viewModel;
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

        viewModel = new ViewModelProvider(RegisterActivity.this).get(LoginViewModel.class);
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewModel.getProgressLoading().observe(RegisterActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.ivMask.setVisibility(View.GONE);
                    binding.progressLoading.setVisibility(View.GONE);
                }
                else {
                    binding.ivMask.setVisibility(View.VISIBLE);
                    binding.progressLoading.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getSignUpSuccess().observe(RegisterActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
                    binding.ivMask.setVisibility(View.VISIBLE);
                    binding.progressLoading.setVisibility(View.VISIBLE);

                    UserData userData = new UserData();
                    userData.setPhoneNumber(String.valueOf(binding.etPhoneNumber.getText()));
                    userData.setFullName(String.valueOf(binding.etFullName.getText()));
                    userData.setPassword(String.valueOf(binding.etPassword.getText()));
                    userData.setIdMode(1);

                    UserRequest userRequest = new UserRequest();
                    userRequest.setData(userData);
                    viewModel.signUpUser(userRequest,getApplicationContext());


                }
            }
        });
    }
}
