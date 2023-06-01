package com.group11.shoppuka.project.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.databinding.FragmentAccountPageBinding;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.view.LoginActivity;

public class AccountPageFragment extends Fragment {


    private FragmentAccountPageBinding binding;


    private void logOutAccount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyApplication.KEY_LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MyApplication.KEY_ACCOUNT_PHONE);
        editor.remove(MyApplication.FULL_NAME_PHONE);
        editor.remove(MyApplication.ID_MODE);
        editor.apply();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutAccount(getActivity());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }
}