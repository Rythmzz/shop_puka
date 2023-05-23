package com.group11.shoppuka.baitap_thuchanh.bt4;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.databinding.L4ActivityInformationRegisterBinding;

public class L4InformationRegister extends AppCompatActivity {
    L4ActivityInformationRegisterBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = L4ActivityInformationRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnRegister.setOnClickListener(view1 ->
        {
            binding.l4ShowTextTaiKhoan.setText("Tài khoản: "+binding.l4TextTaiKhoan.getText());
            binding.l4ShowTextMatKhau.setText("Mật khẩu: "+binding.l4TextMatKhau.getText());
            binding.l4ShowTextNgaySinh.setText("Ngày Sinh: "+binding.l4TextNgaySinh.getText());
            binding.l4ShowTextEmail.setText("Email: "+binding.l4TextEmail.getText());
        });
    }
}
