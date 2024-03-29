package com.group11.shoppuka.project.view.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.lifecycle.ViewModelProvider;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityLoginBinding;
import com.group11.shoppuka.project.model.account.AttributesUser;
import com.group11.shoppuka.project.model.account.User;
import com.group11.shoppuka.project.model.account.UserResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.view.home.MainPageActivity;
import com.group11.shoppuka.project.viewmodel.LoginViewModel;

import org.json.JSONException;

import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginPageActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UserResponse userCurrentResponse;
    private AttributesUser userCurrent;
    int idAccount= -1;
    private LoginViewModel viewModel;
    @Inject
    SharedPreferences sharedPreferences;

    private String[] loginInfo;

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.fetchUser();
    }

    private void saveLoginInfo(String phoneNumber,String avatar, String fullName, int idMode, int statusLogin){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MyApplication.KEY_ACCOUNT_PHONE, phoneNumber);
        editor.putString(MyApplication.FULL_NAME_PHONE,fullName);
        editor.putInt(MyApplication.ID_MODE,idMode);
        editor.putInt(MyApplication.STATUS_LOGIN,statusLogin);
        editor.putString(MyApplication.AVATAR_ACCOUNT,avatar);
        editor.apply();
    }

    private String[] getLoginInfo(){
        String phoneNumber = sharedPreferences.getString(MyApplication.KEY_ACCOUNT_PHONE,null);
        String fullName = sharedPreferences.getString(MyApplication.FULL_NAME_PHONE,null);
        String urlAvatar = sharedPreferences.getString(MyApplication.AVATAR_ACCOUNT,null);
        int idMode = sharedPreferences.getInt(MyApplication.ID_MODE,-1);
        int statusLogin = sharedPreferences.getInt(MyApplication.STATUS_LOGIN,-1);
        return new String[]{phoneNumber,fullName,String.valueOf(idMode),String.valueOf(statusLogin),urlAvatar};
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setIntialData();
        checkRememberLogin();
        setObserverData();
        setUI();
        setEventHandler();
    }

    private void setEventHandler() {
        binding.tvRegister.setOnClickListener( view1 ->{
            Intent intent = new Intent(this, RegisterPageActivity.class);
            startActivity(intent);
        });


        binding.btnLogin.setOnClickListener(view1 -> {
            if (binding.etAccount.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Vui lòng không để trống số điện thoại!",Toast.LENGTH_LONG).show();
                binding.etAccount.setError("Vui lòng không để trống số điện thoại!");
            }
            else if (binding.etPassword.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Vui lòng không để trống mật khẩu!",Toast.LENGTH_LONG).show();
                binding.etPassword.setError("Vui lòng không để trống mật khẩu!");
            }
            else {
                if (userCurrentResponse.getData() != null){
                    for (User user : userCurrentResponse.getData()){
                        if (String.valueOf(binding.etAccount.getText()).equals(user.getAttributes().getPhoneNumber())
                                && String.valueOf(binding.etPassword.getText()).equals(user.getAttributes().getPassword())){
                            userCurrent = user.getAttributes();
                            idAccount = user.getId();
                            break;
                        }
                    }
                    if (userCurrent != null) {
                        if (binding.cbRemember.isChecked())
                            saveLoginInfo(userCurrent.getPhoneNumber(), userCurrent.getImageURL(),userCurrent.getFullName(), userCurrent.getIdMode(), 1);
                        else saveLoginInfo(userCurrent.getPhoneNumber(),userCurrent.getImageURL(),userCurrent.getFullName(), userCurrent.getIdMode(), -1);
                        Intent intent =new Intent(getApplicationContext(), MainPageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle infoUser = new Bundle();
                        infoUser.putString(MyApplication.KEY_ACCOUNT_PHONE,userCurrent.getPhoneNumber());
                        infoUser.putString(MyApplication.FULL_NAME_PHONE,userCurrent.getFullName());
                        infoUser.putString(MyApplication.ID_MODE,String.valueOf(userCurrent.getIdMode()));
                        infoUser.putString(MyApplication.AVATAR_ACCOUNT,userCurrent.getImageURL());
                        infoUser.putInt(MyApplication.ID_ACCOUNT,idAccount);
                        intent.putExtras(infoUser);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginPageActivity.this, "Đăng nhập thất bại! Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        viewModel.fetchUser();
                    }
                }
                else {
                    Toast.makeText(this,"Kiểm tra IP ở other/MyApplication.class/localHost !!", Toast.LENGTH_LONG).show();
                    viewModel.fetchUser();
                }

            }

        });
    }

    private void setUI() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Đăng Nhập");
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
    }

    private void setObserverData() {
        viewModel.getListUser().observe(LoginPageActivity.this, userResponse -> userCurrentResponse = userResponse);
        viewModel.fetchUser();
    }

    private void checkRememberLogin() {
        if (loginInfo[0] != null && loginInfo[1] != null && loginInfo[3].equals("1")){
            Intent intent =new Intent(getApplicationContext(),MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle infoUser = new Bundle();
            infoUser.putString(MyApplication.KEY_ACCOUNT_PHONE,loginInfo[0]);
            infoUser.putString(MyApplication.FULL_NAME_PHONE,loginInfo[1]);
            infoUser.putString(MyApplication.AVATAR_ACCOUNT,loginInfo[4]);
            infoUser.putString(MyApplication.ID_MODE,loginInfo[2]);
            intent.putExtras(infoUser);
            startActivity(intent);
            finish();
        }
    }

    private void setIntialData() {
        loginInfo = getLoginInfo();

        viewModel = new ViewModelProvider(LoginPageActivity.this).get(LoginViewModel.class);

        userCurrentResponse = new UserResponse();
    }

    private void confirmOtp(Context context) throws JSONException {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

       //  Button buttonConfirm = (Button) confirmDialog.findViewById(R.id.buttonConfirm);
        Button buttonResend = (Button) confirmDialog.findViewById(R.id.buttonResend);

        EditText otpNumber1 = (EditText) confirmDialog.findViewById(R.id.otpNumber1);
        EditText otpNumber2 = (EditText) confirmDialog.findViewById(R.id.otpNumber2);
        EditText otpNumber3 = (EditText) confirmDialog.findViewById(R.id.otpNumber3);
        EditText otpNumber4 = (EditText) confirmDialog.findViewById(R.id.otpNumber4);
        EditText otpNumber5 = (EditText) confirmDialog.findViewById(R.id.otpNumber5);
        EditText otpNumber6 = (EditText) confirmDialog.findViewById(R.id.otpNumber6);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(confirmDialog);

        final AlertDialog alertDialog = alert.create();

        alertDialog.show();

        otpNumber1.setText("2");
        otpNumber2.setText("5");
        otpNumber3.setText("1");
        otpNumber4.setText("2");
        otpNumber5.setText("0");
        otpNumber6.setText("1");


        alertDialog.setOnDismissListener(dialogInterface -> userCurrent = null);

        //Khi nhấn nút xác nhận từ hộp thoại cảnh báo
//        buttonConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Lấy mã OTP do người dùng nhập từ EditText
////                final String otp = editTextConfirmOtp.getText().toString().trim();
//                if (binding.cbRemember.isChecked())
//                    saveLoginInfo(userCurrent.phoneNumber, userCurrent.imageURL,userCurrent.fullName, userCurrent.idMode, 1);
//                else saveLoginInfo(userCurrent.phoneNumber,userCurrent.imageURL,userCurrent.fullName, userCurrent.idMode, -1);
//                alertDialog.dismiss();
//                Intent intent =new Intent(getApplicationContext(), MainPageActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                Bundle infoUser = new Bundle();
//                infoUser.putString(MyApplication.KEY_ACCOUNT_PHONE,userCurrent.getPhoneNumber());
//                infoUser.putString(MyApplication.FULL_NAME_PHONE,userCurrent.getFullName());
//                infoUser.putString(MyApplication.ID_MODE,String.valueOf(userCurrent.getIdMode()));
//                infoUser.putString(MyApplication.AVATAR_ACCOUNT,userCurrent.getImageURL());
//                infoUser.putInt(MyApplication.ID_ACCOUNT,idAccount);
//                intent.putExtras(infoUser);
//                startActivity(intent);
//                finish();
//
//            }
//        });





        buttonResend.setOnClickListener(v -> {
            buttonResend.setEnabled(false);

            new CountDownTimer(30000, 1000) {
                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    buttonResend.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds");
                }

                @SuppressLint("SetTextI18n")
                public void onFinish() {
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
        });
    }
}
