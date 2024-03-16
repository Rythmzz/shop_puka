package com.group11.shoppuka.project.view.home.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.project.view.checkout.CheckoutPageActivity;
import com.group11.shoppuka.databinding.FragmentCartPageBinding;
import com.group11.shoppuka.project.adapter.CartListAdapter;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.viewmodel.CartViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CartPageFragment extends Fragment {

    FragmentCartPageBinding binding;
    private CartListAdapter cartListAdapter;
    private CartViewModel cartViewModel;
    private ProductViewModel productViewModel;
    private CartResponse currentCartResponse;



    @Override
    public void onResume() {
        super.onResume();
        productViewModel.fetchData();
       cartViewModel.fetchListCart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        intialData();
        setObserverData();
        setEventHandler();
        return view;
    }

    private void setEventHandler() {
        binding.btnBuy.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CheckoutPageActivity.class);
            intent.putExtra(MyApplication.KEY_GET_LISTCART,currentCartResponse);
            view.getContext().startActivity(intent);
        });
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void setObserverData() {
        cartViewModel.getCartResponseMutableLiveData().observe(requireActivity(), cartResponse -> {
            if (cartResponse.getData().size() == 0 ) binding.csNotCart.setVisibility(View.VISIBLE);
            else binding.csNotCart.setVisibility(View.GONE);
            cartListAdapter.setCartResponse(cartResponse);
            int totalPrice = 0;
            currentCartResponse = new CartResponse();
            currentCartResponse.setData(cartResponse.getData());
            for (Cart cart : cartResponse.getData()) totalPrice+= (cart.getAttributes().getTotalPrice() * cart.getAttributes().getCount());
            binding.totalPrice.setText(MyApplication.formatCurrency(String.valueOf(totalPrice)) + " VNĐ");
            cartListAdapter.notifyDataSetChanged();
        });
        cartViewModel.fetchListCart();

        productViewModel.getProductResponseLiveData().observe(requireActivity(), productResponse -> {
            cartListAdapter.setProductResponse(productResponse);
            cartListAdapter.notifyDataSetChanged();
        });

        productViewModel.getProductIsAvalible().observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        binding.recyclerViewCart.setAdapter(cartListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewCart.setLayoutManager(layoutManager);


    }

    private void intialData() {
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        cartListAdapter = new CartListAdapter(new CartResponse(),new ProductResponse(), cartViewModel);


    }

}