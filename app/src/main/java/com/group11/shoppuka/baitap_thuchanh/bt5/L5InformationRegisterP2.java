package com.group11.shoppuka.baitap_thuchanh.bt5;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.R;
import com.group11.shoppuka.baitap_thuchanh.bt5.model.SoThich;
import com.group11.shoppuka.databinding.L5ActivityInformationRegisterP2Binding;

public class L5InformationRegisterP2 extends AppCompatActivity {
    L5ActivityInformationRegisterP2Binding binding;
    SoThich[] stList = new SoThich[3];
    String radioText = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = L5ActivityInformationRegisterP2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        stList[0]= new SoThich("Chơi game",false);
        stList[1]=new SoThich("Đọc sách",false);
        stList[2]=new SoThich("Đọc báo",false);


        binding.btnSend.setOnClickListener(view1 ->
        {
            binding.l5ShowTextHoTen.setText("Họ tên: "+binding.l5TextHoTen.getText());
            binding.l5ShowTextNgaySinh.setText("Ngày sinh: "+binding.l5TextNgaySinh.getText());
            binding.l5ShowTextBangCap.setText("Bằng cấp: "+radioText);
            String result = "";
            for (int i = 0 ; i < 3 ; i++){
                if (stList[i].trangThai == true){
                    result += stList[i].tenSoThich + " ";
                }
            }
            binding.l5ShowTextSoThich.setText("Sở thích: "+result);
            binding.l5ShowTextOther.setText("Thông tin khác: "+binding.l5TextThongTinKhac.getText());

        });



    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.l5_radio1:
                if (checked)
                    radioText = "Đại Học";
                    break;
            case R.id.l5_radio2:
                if (checked)
                    radioText = "Cao Đẳng";
                    break;
            case R.id.l5_radio3:
                if(checked)
                    radioText = "Trung Cấp";
                break;
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cb1:
                if (checked)
                stList[0].trangThai = true;
            else
                    stList[0].trangThai = false;
                break;
            case R.id.cb2:
                if (checked)
                    stList[1].trangThai = true;
                else
                    stList[1].trangThai = false;
                break;
            case R.id.cb3:
                if (checked)
                    stList[2].trangThai = true;
            else
                    stList[2].trangThai = false;
                break;
            // TODO: Veggie sandwich
        }
    }
}
