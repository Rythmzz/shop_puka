package com.group11.shoppuka.project.view.login;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityRegisterBinding;
import com.group11.shoppuka.project.model.account.UserData;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.viewmodel.LoginViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterPageActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    private LoginViewModel viewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setIntialData();
        setUI();
        setEventHandler();
        setObserverData();
    }

    private void setObserverData() {
        viewModel.getProgressLoading().observe(RegisterPageActivity.this, aBoolean -> {
            if (aBoolean){
                binding.ivMask.setVisibility(View.GONE);
                binding.progressLoading.setVisibility(View.GONE);
            }
            else {
                binding.ivMask.setVisibility(View.VISIBLE);
                binding.progressLoading.setVisibility(View.VISIBLE);
            }
        });
        viewModel.getSignUpSuccess().observe(RegisterPageActivity.this, aBoolean -> {
            if (aBoolean){
                Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setIntialData() {
        viewModel = new ViewModelProvider(RegisterPageActivity.this).get(LoginViewModel.class);
    }

    private void setEventHandler() {
        binding.tvLogin.setOnClickListener(view -> finish());
        binding.btnRegister.setOnClickListener(view -> {
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
                userData.setImageURL("none");

                UserRequest userRequest = new UserRequest();
                userRequest.setData(userData);
                viewModel.signUpUser(userRequest,getApplicationContext());
            }
        });
    }

    private void setUI() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Đăng Ký");
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
    }
}
