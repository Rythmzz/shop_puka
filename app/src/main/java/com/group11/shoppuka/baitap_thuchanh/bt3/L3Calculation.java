package com.group11.shoppuka.baitap_thuchanh.bt3;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.L3ActivityCalculationBinding;

public class L3Calculation extends AppCompatActivity {

    private L3ActivityCalculationBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = L3ActivityCalculationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
            binding.btnCong.setOnClickListener(view1 -> {
                try{
                    double result = Double.parseDouble(String.valueOf(binding.etNumber1.getText())) +
                            Double.parseDouble(String.valueOf(binding.etNumber2.getText()));
                    binding.tfTextResult.setText(String.valueOf(result));
                }catch(Exception ex){
                    binding.tfTextResult.setText("Không đủ 2 số để thực hiện phép tính!!");
                }
            });
            binding.btnTru.setOnClickListener(view1 -> {
                try{
                    double result = Double.parseDouble(String.valueOf(binding.etNumber1.getText())) -
                            Double.parseDouble(String.valueOf(binding.etNumber2.getText()));
                    binding.tfTextResult.setText(String.valueOf(result));
                }catch(Exception ex){
                    binding.tfTextResult.setText("Không đủ 2 số để thực hiện phép tính!!");
                }
            });
            binding.btnNhan.setOnClickListener(view1 -> {
                try{
                    double result = Double.parseDouble(String.valueOf(binding.etNumber1.getText())) *
                            Double.parseDouble(String.valueOf(binding.etNumber2.getText()));
                    binding.tfTextResult.setText(String.valueOf(result));
                }catch(Exception ex){
                    binding.tfTextResult.setText("Không đủ 2 số để thực hiện phép tính!!");
                }
            });
            binding.btnChia.setOnClickListener(view1 -> {
                if(Double.parseDouble(String.valueOf(binding.etNumber1.getText())) == 0){
                    binding.tfTextResult.setText("Dạng Vô Định !!");
                }
                else {
                    try{
                        double result = Double.parseDouble(String.valueOf(binding.etNumber1.getText())) /
                                Double.parseDouble(String.valueOf(binding.etNumber2.getText()));
                        binding.tfTextResult.setText(String.valueOf(result));
                    }catch(Exception ex){
                        binding.tfTextResult.setText("Không đủ 2 số để thực hiện phép tính!!");
                    }
                }
            });
    }
}
