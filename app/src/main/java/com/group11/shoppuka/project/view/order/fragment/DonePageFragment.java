package com.group11.shoppuka.project.view.order.fragment;

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
import com.group11.shoppuka.databinding.FragmentDonePageBinding;
import com.group11.shoppuka.project.adapter.OrderListDoneAdapter;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.OrderViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DonePageFragment extends Fragment {

    FragmentDonePageBinding binding;

    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;

    private OrderListDoneAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        productViewModel.fetchData();
        orderViewModel.fetchListData(2,3);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDonePageBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();
        setIntitalData();
        setObseverData();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        binding.recyclerViewOrder.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewOrder.setLayoutManager(linearLayoutManager);
    }

    private void setObseverData() {
        productViewModel.getProductResponseLiveData().observe(requireActivity(), productResponse -> {
            adapter.setProductResponse(productResponse);
            adapter.notifyDataSetChanged();
        });

        orderViewModel.getOrderResponseMutableLiveData().observe(requireActivity(), new Observer<OrderResponse>() {
            @Override
            public void onChanged(OrderResponse orderResponse) {
                adapter.setOrderResponse(orderResponse);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setIntitalData() {
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        adapter = new OrderListDoneAdapter(new OrderResponse(),new ProductResponse());
    }
}
