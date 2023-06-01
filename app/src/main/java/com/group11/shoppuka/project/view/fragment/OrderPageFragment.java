package com.group11.shoppuka.project.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.databinding.FragmentOrderPageBinding;
import com.group11.shoppuka.project.adapter.OrderListAdapter;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.viewmodel.OrderViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

public class OrderPageFragment extends Fragment {



    private FragmentOrderPageBinding binding;

    private OrderViewModel orderViewModel;
    private ProductViewModel productViewModel;

    private OrderListAdapter orderListAdapter;

    String phoneNumber = "";

    @Override
    public void onResume() {
        super.onResume();
        orderViewModel.fetchData(phoneNumber);
        productViewModel.fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderPageBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);
        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyApplication.KEY_LOGIN, Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString(MyApplication.KEY_ACCOUNT_PHONE,null);


        orderListAdapter = new OrderListAdapter(new OrderResponse(), new ProductResponse());

        orderViewModel.getOrderResponseMutableLiveData().observe(getActivity(), new Observer<OrderResponse>() {
            @Override
            public void onChanged(OrderResponse orderResponse) {
                if (orderResponse.getData().size() == 0 ) binding.csNotCart.setVisibility(View.VISIBLE);
                else binding.csNotCart.setVisibility(View.GONE);
                orderListAdapter.setOrderResponse(orderResponse);
                orderListAdapter.notifyDataSetChanged();
            }
        });

        productViewModel.getProductResponseLiveData().observe(getActivity(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                orderListAdapter.setProductResponse(productResponse);
                orderListAdapter.notifyDataSetChanged();
            }
        });

        binding.recyclerViewOrder.setAdapter(orderListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewOrder.setLayoutManager(linearLayoutManager);



        return view;
    }
}