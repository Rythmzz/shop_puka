package com.group11.shoppuka.project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.FragmentHomePageBinding;
import com.group11.shoppuka.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }
    public class StartGameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Xác Nhận Đăng Nhập")
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(TextUtils.isEmpty(binding.l4TextTaiKhoan.getText()) || TextUtils.isEmpty(binding.l4TextMatKhau.getText())){
                                binding.textError.setVisibility(View.VISIBLE);
                                binding.layoutInfo.setVisibility(View.GONE);
                            }
                            else{
                                binding.textUser.setText(binding.l4TextTaiKhoan.getText());
                                binding.textPassword.setText(binding.l4TextMatKhau.getText());
                                binding.textError.setVisibility(View.GONE);
                                binding.layoutInfo.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private static String LOGIN_KEY = "login_info";
    private static String ACCOUNT_PHONE = "phone_info";
    private static String PASSWORD_PHONE = "password_phone_info";

    private void saveLoginInfo(Context context, String phoneNumber, String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCOUNT_PHONE, phoneNumber);
        editor.putString("password",password);
        editor.apply();
    }

    private String[] getLoginInfo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_KEY,Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(ACCOUNT_PHONE,null);
        String password = sharedPreferences.getString(PASSWORD_PHONE,null);
        return new String[]{phoneNumber,password};
    }
    private FragmentLoginBinding binding;
    ProgressDialog progressdialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.btnLogin.setOnClickListener(view1 ->
        {
            progressdialog = new ProgressDialog(getContext());
            progressdialog.setTitle("Đăng Nhập");
            progressdialog.setMessage("Loading....");
            progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressdialog.setMax(100);
            progressdialog.show();
            handle.sendMessage(handle.obtainMessage());
            new CountDownTimer(5000, 1000) {
                                public void onTick(long millisUntilFinished) {
//                                    binding.progressBarCyclic.setVisibility(View.VISIBLE);
                                }

                                public void onFinish() {
                                    progressdialog.dismiss();
//                                    binding.progressBarCyclic.setVisibility(View.GONE);
                                    if(TextUtils.isEmpty(binding.l4TextTaiKhoan.getText()) || TextUtils.isEmpty(binding.l4TextMatKhau.getText())){
                                        binding.textError.setVisibility(View.VISIBLE);
                                        binding.layoutInfo.setVisibility(View.GONE);
                                    }
                                    else{
                                        binding.textUser.setText(binding.l4TextTaiKhoan.getText());
                                        binding.textPassword.setText(binding.l4TextMatKhau.getText());
                                        binding.textError.setVisibility(View.GONE);
                                        binding.layoutInfo.setVisibility(View.VISIBLE);
                                    }
                                }

                            }.start();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        while (progressdialog.getProgress() <= progressdialog
//                                .getMax()) {
//                            Thread.sleep(200);
//                            handle.sendMessage(handle.obtainMessage());
//                            if (progressdialog.getProgress() == progressdialog
//                                    .getMax()) {
//                                progressdialog.dismiss();
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();


//            new ProgressDialog.Builder(getContext())
//                    .setTitle("Đăng Nhập")
//                    .setMessage("Xác nhận Đăng Nhập")
//
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton("xác nhận", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            new CountDownTimer(5000, 1000) {
//
//                                public void onTick(long millisUntilFinished) {
//                                    binding.progressBarCyclic.setVisibility(View.VISIBLE);
//                                }
//
//                                public void onFinish() {
//                                    binding.progressBarCyclic.setVisibility(View.GONE);
//                                    if(TextUtils.isEmpty(binding.l4TextTaiKhoan.getText()) || TextUtils.isEmpty(binding.l4TextMatKhau.getText())){
//                                        binding.textError.setVisibility(View.VISIBLE);
//                                        binding.layoutInfo.setVisibility(View.GONE);
//                                    }
//                                    else{
//                                        binding.textUser.setText(binding.l4TextTaiKhoan.getText());
//                                        binding.textPassword.setText(binding.l4TextMatKhau.getText());
//                                        binding.textError.setVisibility(View.GONE);
//                                        binding.layoutInfo.setVisibility(View.VISIBLE);
//                                    }
//                                }
//
//                            }.start();
//                        }
//                    })
//
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton("hủy bỏ", null)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
        });


        return view;
    }
    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressdialog.incrementProgressBy(1);
        }
    };


}
