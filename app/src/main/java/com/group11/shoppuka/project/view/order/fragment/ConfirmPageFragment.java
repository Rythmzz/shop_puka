package com.group11.shoppuka.project.view.order.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.group11.shoppuka.databinding.FragmentConfirmPageBinding;
import com.group11.shoppuka.project.adapter.OrderListConfirmAdapter;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.OrderViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConfirmPageFragment extends Fragment {

    FragmentConfirmPageBinding binding;
    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;

    private boolean isFirstSet = false;
    private OrderListConfirmAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        productViewModel.fetchData();
        orderViewModel.fetchListData(0,0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentConfirmPageBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();
        setIntialData();
        setObserverData();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        binding.recyclerViewOrder.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewOrder.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setObserverData() {
        productViewModel.getProductResponseLiveData().observe(requireActivity(), productResponse -> {
            if (!isFirstSet ){
                adapter.setProductResponse(productResponse);
                adapter.notifyDataSetChanged();
                isFirstSet = true;
            }
        });

        orderViewModel.getOrderResponseMutableLiveData().observe(requireActivity(), orderResponse -> {
            adapter.setOrderResponse(orderResponse);
            adapter.notifyDataSetChanged();
        });
    }

    private void setIntialData() {
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        adapter = new OrderListConfirmAdapter(new OrderResponse(),new ProductResponse(), orderViewModel);
    }
}
