package com.group11.shoppuka.project.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityHomepageBinding;


public class MainPageActivity extends AppCompatActivity {
    ActivityHomepageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Home");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F87217"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (savedInstanceState == null){
            Fragment fragment = new HomePageFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
        binding.bottomNavigationView.setBackgroundColor(Color.WHITE);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){


                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment;
                    switch (item.getItemId()){
                        case R.id.Home:
                            getSupportActionBar().setTitle("Home");
                            fragment = new HomePageFragment();
                            loadFragment(fragment);
                            return true;
                        case R.id.Cate:
                            getSupportActionBar().setTitle("Category");
                            fragment = new CategoryPageFragment();
                            loadFragment(fragment);
                            return true;
                        case R.id.Search:
                            getSupportActionBar().setTitle("Search");
                            fragment = new SearchPageFragment();
                            loadFragment(fragment);
                            return true;
                        case R.id.Order:
                            getSupportActionBar().setTitle("Order");
                            fragment = new OrderPageFragment();
                            loadFragment(fragment);
                            return true;
                        case R.id.Account:
                            getSupportActionBar().setTitle("Account");
                            fragment = new AccountPageFragment();
                            loadFragment(fragment);
                            return true;
                        default:


                    }
                    return false;
                }

            };
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_menu){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }


}

//    SliderAdapter sliderAdapter = new SliderAdapter(images);
//        sliderView.setSliderAdapter(sliderAdapter);
//                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
//                sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
//                sliderView.startAutoCycle();