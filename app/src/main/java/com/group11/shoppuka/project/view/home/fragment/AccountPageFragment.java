package com.group11.shoppuka.project.view.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.databinding.FragmentAccountPageBinding;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.view.login.LoginPageActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountPageFragment extends Fragment {

    FragmentAccountPageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        setEventHandler();
        return view;

    }

    private void setEventHandler() {
        binding.btnLogOut.setOnClickListener(view -> {
            logOutAccount(requireActivity());
            Intent intent = new Intent(requireActivity(), LoginPageActivity.class);
            startActivity(intent);
        });
    }

    private void logOutAccount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyApplication.KEY_LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MyApplication.KEY_ACCOUNT_PHONE);
        editor.remove(MyApplication.FULL_NAME_PHONE);
        editor.remove(MyApplication.ID_MODE);
        editor.apply();

    }
}