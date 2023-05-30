package com.group11.shoppuka.project.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.group11.shoppuka.CartActivity;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityHomepageBinding;
import com.group11.shoppuka.databinding.HeaderDrawerBinding;
import com.group11.shoppuka.project.view.fragment.AccountPageFragment;
import com.group11.shoppuka.project.view.fragment.CartPageFragment;
import com.group11.shoppuka.project.view.fragment.HomePageFragment;
import com.group11.shoppuka.project.view.fragment.OrderPageFragment;
import com.group11.shoppuka.project.view.fragment.SearchPageFragment;


public class MainPageActivity extends AppCompatActivity implements HomePageFragment.OnItemSelectedListener {
    ActivityHomepageBinding binding;

    private static String LOGIN_KEY = "login_info";
    private static String ACCOUNT_PHONE = "phone_info";
    private static String FULL_NAME_PHONE = "fullName_phone_info";
    private static String ID_MODE = "id_mode_info";

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,binding.drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            Fragment fragment = new HomePageFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        binding.bottomNavigationView.setBackgroundColor(Color.WHITE);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Log.i("FragmentManager",String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

        View headerView = binding.navigationView.getHeaderView(0);
        HeaderDrawerBinding headerDrawerBinding = HeaderDrawerBinding.bind(headerView);
        Intent intentInfoUser = getIntent();
        Bundle infoUser = intentInfoUser.getExtras();
        headerDrawerBinding.tvPhoneNumber.setText("Số điện thoại: "+infoUser.getString(ACCOUNT_PHONE));
        headerDrawerBinding.tvFullName.setText("Tên người dùng: "+ infoUser.get(FULL_NAME_PHONE));

        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_KEY,MODE_PRIVATE);
        int idMode = sharedPreferences.getInt(ID_MODE,-1);

        if (idMode != 0){
            Menu menu = binding.navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.modeAuthentication);
            menuItem.setVisible(false);

        }
        binding.navigationView.setNavigationItemSelectedListener(mOnDrawerLayoutItemSelectedListener);




    }


    private NavigationView.OnNavigationItemSelectedListener mOnDrawerLayoutItemSelectedListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.modeAuthentication:{
                            Intent intent = new Intent(MainPageActivity.this, ManageProductPageActivity.class);
                            startActivity(intent);
                        }
                        case R.id.manageOrder:{
                            Intent intent = new Intent(MainPageActivity.this,ManageOrderPageActivity.class);
                            startActivity(intent);
                        }
                    }
                    return false;
                }
            };


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
                        case R.id.Cart:
                            getSupportActionBar().setTitle("Cart");
                            fragment = new CartPageFragment();
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
            binding.drawerLayout.openDrawer(GravityCompat.START);
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onItemSelected(int itemID) {
        binding.bottomNavigationView.setSelectedItemId(itemID);
    }
}

//    SliderAdapter sliderAdapter = new SliderAdapter(images);
//        sliderView.setSliderAdapter(sliderAdapter);
//                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
//                sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
//                sliderView.startAutoCycle();