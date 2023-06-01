package com.group11.shoppuka.project.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.group11.shoppuka.databinding.FragmentConfirmPageBinding;
import com.group11.shoppuka.project.adapter.OrderListConfirmAdapter;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.OrderViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

public class ConfirmPageFragment extends Fragment {

    private FragmentConfirmPageBinding binding;

    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;

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

        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);

        adapter = new OrderListConfirmAdapter(new OrderResponse(),new ProductResponse());

        productViewModel.getProductResponseLiveData().observe(getActivity(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                adapter.setProductResponse(productResponse);
                adapter.notifyDataSetChanged();
            }
        });

        orderViewModel.getOrderResponseMutableLiveData().observe(getActivity(), new Observer<OrderResponse>() {
            @Override
            public void onChanged(OrderResponse orderResponse) {
                adapter.setOrderResponse(orderResponse);
                adapter.notifyDataSetChanged();
            }
        });

        binding.recyclerViewOrder.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewOrder.setLayoutManager(linearLayoutManager);

        return view;
    }
}
