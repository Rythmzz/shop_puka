package com.group11.shoppuka.project.view.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityHomepageBinding;
import com.group11.shoppuka.databinding.HeaderDrawerBinding;
import com.group11.shoppuka.project.model.account.UserData;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.view.about.ActivityAboutPage;
import com.group11.shoppuka.project.view.login.LoginPageActivity;
import com.group11.shoppuka.project.view.order.ManageOrderPageActivity;
import com.group11.shoppuka.project.view.product.ManageProductPageActivity;
import com.group11.shoppuka.project.view.home.fragment.AccountPageFragment;
import com.group11.shoppuka.project.view.home.fragment.CartPageFragment;
import com.group11.shoppuka.project.view.home.fragment.HomePageFragment;
import com.group11.shoppuka.project.view.order.fragment.OrderPageFragment;
import com.group11.shoppuka.project.view.home.fragment.SearchPageFragment;
import com.group11.shoppuka.project.viewmodel.CartViewModel;
import com.group11.shoppuka.project.viewmodel.UserViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@AndroidEntryPoint
public class MainPageActivity extends AppCompatActivity implements HomePageFragment.OnItemSelectedListener {
    private ActivityHomepageBinding binding;
    private String imageUrl;
    private final UserData userData = new UserData();
    private HeaderDrawerBinding headerDrawerBinding;
    private UserViewModel viewModel;

    private CartViewModel cartViewModel;
    private Bundle infoUser;
    private int idMode;

    private BadgeDrawable badge;

    @Inject
    SharedPreferences sharedPreferences;
    @Override
    protected void onResume() {
        super.onResume();
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (cartViewModel != null){
            cartViewModel.fetchListCart();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setIntialData();
        setUI(savedInstanceState);
        setEventHandler();
    }

    private void setEventHandler() {
        headerDrawerBinding.avatarAccount.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), MyApplication.PICK_IMAGE);


        });
    }
    @SuppressLint("SetTextI18n")
    private void setUI(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Trang Chủ");
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));

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
        headerDrawerBinding = HeaderDrawerBinding.bind(headerView);
        Intent intentInfoUser = getIntent();
        infoUser = intentInfoUser.getExtras();

        headerDrawerBinding.tvUser.setText("User#"+infoUser.getString(MyApplication.KEY_ACCOUNT_PHONE));
        headerDrawerBinding.tvPhoneNumber.setText("Số điện thoại: "+infoUser.getString(MyApplication.KEY_ACCOUNT_PHONE));
        headerDrawerBinding.tvFullName.setText("Tên người dùng: "+ infoUser.get(MyApplication.FULL_NAME_PHONE));
        if (!infoUser.getString(MyApplication.AVATAR_ACCOUNT).equals("none")){
            Glide.with(this).load(MyApplication.localHost +"/" + infoUser.getString(MyApplication.AVATAR_ACCOUNT)).into(headerDrawerBinding.avatarAccount);
        }

        if (idMode != 0){
            Menu menu = binding.navigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.modeAuthentication);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.manageOrder);
            menuItem.setVisible(false);
            Menu menuBottom = binding.bottomNavigationView.getMenu();
            menuItem = menuBottom.findItem(R.id.order);
            menuItem.setVisible(true);
        }
        binding.navigationView.setNavigationItemSelectedListener(mOnDrawerLayoutItemSelectedListener);


        cartViewModel.getCartResponseMutableLiveData().observe(this, cartResponse -> {
            badge.setVisible(true);
            badge.setNumber(cartResponse.getData().size());
        });

        cartViewModel.fetchListCart();



    }

    private void setIntialData() {
        idMode = sharedPreferences.getInt(MyApplication.ID_MODE,-1);
        badge = binding.bottomNavigationView.getOrCreateBadge(R.id.cart);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);


    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyApplication.PICK_IMAGE){
           if (resultCode == RESULT_OK){
               Uri imageUri = Objects.requireNonNull(data).getData();
               headerDrawerBinding.avatarAccount.setImageURI(imageUri);
               try {
                   InputStream inputStream =getContentResolver().openInputStream(imageUri);
                   byte[] imageData = readBytes(inputStream);
                   RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)),imageData);
                   MultipartBody.Part image = MultipartBody.Part.createFormData("files","image.jpg",requestFile);
                   RetrofitService retrofitService = new RetrofitService();
                   ApiService apiService = retrofitService.retrofit.create(ApiService.class);

                   apiService.uploadImage(image).enqueue(new Callback<ResponseBody>() {
                       @Override
                       public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                           if (response.isSuccessful()) {
                               try {
                                   String responseBody = Objects.requireNonNull(response.body()).string();
                                   JSONArray jsonArray = new JSONArray(responseBody);
                                   JSONObject jsonObject = jsonArray.getJSONObject(0);
                                   imageUrl = jsonObject.getString("url");
                                   userData.setImageURL(imageUrl);
                                   System.out.println(imageUrl);
                                   viewModel = new ViewModelProvider(MainPageActivity.this).get(UserViewModel.class);
                                   UserRequest userRequest = new UserRequest();
                                   userRequest.setData(userData);
                                   System.out.println(infoUser.getInt(MyApplication.ID_ACCOUNT));
                                   viewModel.updateAvatarUser(infoUser.getInt(MyApplication.ID_ACCOUNT), userRequest);

                               }  catch (JSONException | IOException e) {
                                   throw new RuntimeException(e);
                               }
                               System.out.println("upload hình thành công");
                           } else {
                               int statusCode = response.code();
                               ResponseBody errorBody = response.errorBody();
                               String errorMessage;
                               try {
                                   errorMessage = Objects.requireNonNull(response.errorBody()).string();
                               } catch (IOException e) {
                                   throw new RuntimeException(e);
                               }
                               System.out.println(statusCode);
                               System.out.println(errorMessage);
                               try {
                                   errorMessage = errorBody != null ? errorBody.string() : "";
                                   System.out.println(errorMessage);
                               } catch (IOException e) {
                                   Toast.makeText(MainPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       }

                       @Override
                       public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                           t.printStackTrace();
                       }
                   });

               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }



        }
    }

    private void logOutAccount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyApplication.KEY_LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(MyApplication.KEY_ACCOUNT_PHONE);
        editor.remove(MyApplication.FULL_NAME_PHONE);
        editor.remove(MyApplication.ID_MODE);
        editor.apply();

    }


    @SuppressLint("NonConstantResourceId")
    private final NavigationView.OnNavigationItemSelectedListener mOnDrawerLayoutItemSelectedListener =
            item -> {
                switch (item.getItemId()){
                    case R.id.modeAuthentication:{
                        Intent intent = new Intent(MainPageActivity.this, ManageProductPageActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.manageOrder:{
                        Intent intent = new Intent(MainPageActivity.this, ManageOrderPageActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.logOut:
                        logOutAccount(MainPageActivity.this);
                        Intent intent = new Intent(MainPageActivity.this, LoginPageActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            };


    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.home:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Trang Chủ");
                        fragment = new HomePageFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.cart:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Giỏ Hàng");
                        fragment = new CartPageFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.search:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Tìm Kiếm");
                        fragment = new SearchPageFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.order:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Đơn Hàng");
                        fragment = new OrderPageFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.account:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Tài Khoản");
                        fragment = new AccountPageFragment();
                        loadFragment(fragment);
                        return true;
                    default:


                }
                return false;
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
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        else if (id == R.id.action_info){
            Intent intent = new Intent(this, ActivityAboutPage.class);
            startActivity(intent);
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
