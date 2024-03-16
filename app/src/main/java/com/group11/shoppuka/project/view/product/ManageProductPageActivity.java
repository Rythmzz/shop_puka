package com.group11.shoppuka.project.view.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityManageProductBinding;
import com.group11.shoppuka.project.adapter.ProductListManageAdapter;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageProductPageActivity extends AppCompatActivity {
    private ProductListManageAdapter productAdapter;

    private ProductViewModel productViewModel;

    private ProductResponse products;

    private ActivityManageProductBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        productViewModel.fetchData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUI();
        setIntialData();
        setAdapter();
        setEventHandler();
        setObserverData();
    }

    private void setUI() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Quản Lý Sản Phẩm");
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setObserverData() {
        productViewModel.getProductResponseLiveData().observe(ManageProductPageActivity.this, productResponse -> {
            productAdapter.setData(productResponse);
            productAdapter.notifyDataSetChanged();
        });
        productViewModel.fetchData();
    }

    private void setIntialData() {
        products = new ProductResponse();

        productViewModel = new ViewModelProvider(ManageProductPageActivity.this).get(ProductViewModel.class);

        productAdapter = new ProductListManageAdapter(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setEventHandler(){
        binding.btnAddAccount.setOnClickListener(v -> {
           Intent intent = new Intent(ManageProductPageActivity.this, AddProductPageActivity.class);
            startActivity(intent);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            productViewModel.fetchData();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_account_optional,menu);
    }

    private void setAdapter(){
        binding.rvAccountManagement.setAdapter(productAdapter);
        binding.rvAccountManagement.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

    }
}
