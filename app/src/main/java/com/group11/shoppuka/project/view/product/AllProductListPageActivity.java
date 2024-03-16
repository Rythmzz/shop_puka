package com.group11.shoppuka.project.view.product;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityAllProductListPageBinding;
import com.group11.shoppuka.project.adapter.ProductListAllAdapter;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.CartViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AllProductListPageActivity extends AppCompatActivity {
    private ActivityAllProductListPageBinding binding;

    private ProductListAllAdapter productListAllAdapter;

    private int idCategory;

    @Inject
    SharedPreferences sharedPreferences;

    private CartViewModel cartViewModel;

    private ProductResponse currentProduct;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllProductListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setIntialData();
        setUI();
    }

    private void setUI() {
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
    }

    private void setIntialData() {


        Intent data = getIntent();

        idCategory = data.getIntExtra("category_id",-1);

        switch (idCategory){
            case 0: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Máy Tính");
                break;
            }
            case 1: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Phần Cứng");
                break;
            }
            case 2: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Laptop");
                break;
            }
            case 3: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Tai Nghe");
                break;
            }
            case 4: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Điện Thoại");
                break;
            }
            case 5: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Bàn Phím");
                break;
            }
            case 6: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Màn Hình");
                break;
            }
            case 7: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Bàn Ghế");
                break;
            }
            case 8: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Tay Cầm");
                break;
            }
            case 9: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Bàn Di Chuột");
                break;
            }
            case 10: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Công Cụ Livestream");
                break;
            }
            case 11: {
                Objects.requireNonNull(getSupportActionBar()).setTitle("Chuột");
                break;
            }
            default:{
                Objects.requireNonNull(getSupportActionBar()).setTitle("Toàn Bộ Sản Phẩm");
            }
            
        }

        currentProduct = (ProductResponse) data.getSerializableExtra("product_list");

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        productListAllAdapter = new ProductListAllAdapter(currentProduct,cartViewModel,sharedPreferences.getString(MyApplication.KEY_ACCOUNT_PHONE,""));

        GridLayoutManager layoutManagerProduct2 = new GridLayoutManager(this,2);
        binding.rcvListProduct.setAdapter(productListAllAdapter);
        binding.rcvListProduct.setLayoutManager(layoutManagerProduct2);
    }
}
