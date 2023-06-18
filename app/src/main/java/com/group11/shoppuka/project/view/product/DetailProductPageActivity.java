package com.group11.shoppuka.project.view.product;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityDetailBinding;
import com.group11.shoppuka.project.model.cart.CartData;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.view.checkout.CheckoutPageActivity;
import com.group11.shoppuka.project.viewmodel.CartViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailProductPageActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private CartViewModel cartViewModel;
    private Intent data;
    private Product currentProduct;
    private String urlImage;
    private String phoneNumber;
    @Inject
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setIntialData();
        setUI();
        setEventHandler();
    }

    private void setEventHandler() {
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartData cartData = new CartData();
                cartData.setIdProduct(currentProduct.getId());
                System.out.println("Phone Number :"+phoneNumber);
                cartData.setPhoneNumber(phoneNumber);
                cartData.setCount(1);
                if (currentProduct.getAttributes().getSalePrice() != 0) {
                    cartData.setTotalPrice(currentProduct.getAttributes().getSalePrice());
                } else {
                    cartData.setTotalPrice(currentProduct.getAttributes().getPrice());
                }
                CartRequest cartRequest = new CartRequest();
                cartRequest.setData(cartData);

                cartViewModel.addCart(cartRequest,view.getContext());
                finish();
            }
        });
        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartData cartData = new CartData();
                cartData.setIdProduct(currentProduct.getId());
                cartData.setPhoneNumber(phoneNumber);
                cartData.setCount(1);
                if (currentProduct.getAttributes().getSalePrice() != 0) {
                    cartData.setTotalPrice(currentProduct.getAttributes().getSalePrice());
                } else {
                    cartData.setTotalPrice(currentProduct.getAttributes().getPrice());
                }
                CartRequest cartRequest = new CartRequest();
                cartRequest.setData(cartData);

                cartViewModel.addCart(cartRequest,view.getContext());
                cartViewModel.fetchListCart(view.getContext());
                cartViewModel.getCartResponseMutableLiveData().observe(DetailProductPageActivity.this, new Observer<CartResponse>() {
                    @Override
                    public void onChanged(CartResponse cartResponse) {
                        Intent intent = new Intent(view.getContext(), CheckoutPageActivity.class);
                        intent.putExtra(MyApplication.KEY_GET_LISTCART,cartResponse);
                        view.getContext().startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void setUI() {
        getSupportActionBar().setTitle(currentProduct.getAttributes().getName().substring(0,Math.min(currentProduct.getAttributes().getName().length(),15))+"...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F87217"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));

        Glide.with(this).load(urlImage).into(binding.imageViewProduct);
        binding.name.setText(currentProduct.getAttributes().getName());
        binding.price.setText(currentProduct.getAttributes().getPrice() + "VNĐ");
        if (currentProduct.getAttributes().getSalePrice() != 0) {
            binding.price.setPaintFlags(binding.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.salePrice.setText(String.valueOf(currentProduct.getAttributes().getSalePrice()) + "VNĐ");
        }
        binding.description.setText(currentProduct.getAttributes().getDescription());

    }

    private void setIntialData() {
        data = getIntent();
        currentProduct = (Product) data.getSerializableExtra("product");
        urlImage = MyApplication.localHost + currentProduct.getAttributes().getImageURL();
        phoneNumber = sharedPreferences.getString(MyApplication.KEY_ACCOUNT_PHONE,null);

        cartViewModel = new ViewModelProvider(DetailProductPageActivity.this).get(CartViewModel.class);
    }
}
