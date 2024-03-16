package com.group11.shoppuka.project.view.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityManageOrderBinding;
import com.group11.shoppuka.project.view.order.fragment.ConfirmPageFragment;
import com.group11.shoppuka.project.view.order.fragment.DonePageFragment;
import com.group11.shoppuka.project.view.order.fragment.ProgressPageFragment;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageOrderPageActivity extends AppCompatActivity {
    private ActivityManageOrderBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUI(savedInstanceState);
    }

    private void setUI(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null){
            Fragment fragment = new ConfirmPageFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("Quản Lý Đơn Hàng");
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        Fragment fragment;
        switch (item.getItemId()){
            case R.id.Confirm:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Xác Nhận Đơn Hàng");
                fragment = new ConfirmPageFragment();
                loadFragment(fragment);
                return true;
            case R.id.Progress:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Xác Nhận Vận Chuyển");
                fragment = new ProgressPageFragment();
                loadFragment(fragment);
                return true;
            case R.id.Done:
                Objects.requireNonNull(getSupportActionBar()).setTitle("Trạng Thái Đơn Hàng");
                fragment = new DonePageFragment();
                loadFragment(fragment);
                return true;
        }
        return false;
    };

    @Override
    public void onBackPressed() {
        finish();
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
