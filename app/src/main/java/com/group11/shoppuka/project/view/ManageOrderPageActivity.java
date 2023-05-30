package com.group11.shoppuka.project.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityManageOrderBinding;
import com.group11.shoppuka.project.view.fragment.ConfirmPageFragment;
import com.group11.shoppuka.project.view.fragment.DonePageFragment;
import com.group11.shoppuka.project.view.fragment.HomePageFragment;
import com.group11.shoppuka.project.view.fragment.ProgressPageFragment;

import org.checkerframework.checker.units.qual.C;

public class ManageOrderPageActivity extends AppCompatActivity {
    private ActivityManageOrderBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null){
            Fragment fragment = new ConfirmPageFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()){
                case R.id.Confirm:
                    getSupportActionBar().setTitle("Confirm");
                    fragment = new ConfirmPageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.Progress:
                    getSupportActionBar().setTitle("Progress");
                    fragment = new ProgressPageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.Done:
                    getSupportActionBar().setTitle("Done");
                    fragment = new DonePageFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
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
