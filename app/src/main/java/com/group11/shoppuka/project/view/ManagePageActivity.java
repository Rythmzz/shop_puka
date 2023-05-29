package com.group11.shoppuka.project.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityManageBinding;
import com.group11.shoppuka.project.adapter.ProductListManageAdapter;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManagePageActivity extends AppCompatActivity {

    private RecyclerView rcvProductManagement;
    private ProductListManageAdapter productAdapter;
    private ImageButton btnAddAccount,btnAddTopBar,btnOptionalAccount;
    private TextView tvScreenTitle;
    private Context mContext;

    private ProductViewModel productViewModel;

    private ProductResponse products;

    private ActivityManageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Management Product");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F87217"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
        // fetchData();
        setControl();
        setEvent();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setEvent(){
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(ManagePageActivity.this, AddProductPageActivity.class);
                startActivity(intent);
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                productViewModel.fetchData();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_account_optional,menu);
    }

    private void setControl(){
        products = new ProductResponse();

        productViewModel = new ViewModelProvider(ManagePageActivity.this).get(ProductViewModel.class);
        productViewModel.fetchData();

        rcvProductManagement = findViewById(R.id.rvAccountManagement);
        btnAddAccount = findViewById(R.id.btnAddAccount);
        btnOptionalAccount = findViewById(R.id.btn_optional_account);

        productAdapter = new ProductListManageAdapter(this);

        productViewModel.getProductResponseLiveData().observe(ManagePageActivity.this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                productAdapter.setData(productResponse);
                productAdapter.notifyDataSetChanged();
            }
        });

        rcvProductManagement.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        rcvProductManagement.setAdapter(productAdapter);
    }
}
