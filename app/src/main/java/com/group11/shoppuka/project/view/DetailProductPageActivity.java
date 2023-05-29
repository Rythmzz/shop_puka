package com.group11.shoppuka.project.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityDetailBinding;
import com.group11.shoppuka.project.model.cart.CartData;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.viewmodel.CartViewModel;

public class DetailProductPageActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;

    private CartViewModel cartViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");
        getSupportActionBar().setTitle(product.getAttributes().getName().substring(0,Math.min(product.getAttributes().getName().length(),15))+"...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F87217"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
        String url = MyApplication.localHost + product.getAttributes().getImageURL();
        Glide.with(this).load(url).into(binding.imageViewProduct);
        binding.name.setText(product.getAttributes().getName());
        binding.price.setText(product.getAttributes().getPrice() + "VNĐ");
        if (product.getAttributes().getSalePrice() != 0) {
            binding.price.setPaintFlags(binding.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.salePrice.setText(String.valueOf(product.getAttributes().getSalePrice()) + "VNĐ");

        }
        binding.description.setText(product.getAttributes().getDescription());
        SharedPreferences sharedPreferences = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phone_info",null);

        cartViewModel = new ViewModelProvider(DetailProductPageActivity.this).get(CartViewModel.class);
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartData cartData = new CartData();
                cartData.setIdProduct(product.getId());
                cartData.setPhoneNumber(phoneNumber);
                cartData.setCount(1);
                if (product.getAttributes().getSalePrice() != 0) {
                    cartData.setTotalPrice(product.getAttributes().getSalePrice());
                } else {
                    cartData.setTotalPrice(product.getAttributes().getPrice());
                }
                CartRequest cartRequest = new CartRequest();
                cartRequest.setData(cartData);

                cartViewModel.addCart(cartRequest,view.getContext());
            }
        });

    }
}
