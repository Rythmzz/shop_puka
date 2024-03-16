package com.group11.shoppuka.project.view.home.fragment;

import android.annotation.SuppressLint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.databinding.FragmentSearchPageBinding;
import com.group11.shoppuka.project.adapter.ProductListFilterAdapter;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchPageFragment extends Fragment {

    FragmentSearchPageBinding binding;

    private ProductListFilterAdapter adapter;

    private ProductViewModel viewModel;

    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        setIntitalData();
        setUI();
        setObserverData();
        setAdapter();
        setEventHandler();
        return view;
    }

    private void setEventHandler() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filterString = s.toString();
                if(s.toString().isEmpty()){
                    binding.tvSearch.setText("");
                    adapter.setProductResponse(new ProductResponse());
                    adapter.notifyDataSetChanged();
                }
                else {
                    binding.tvSearch.setText("Search Result For Keyword \""+ s +"\"");
                    List<Product> productFilteredSearch = viewModel.getProductResponseLiveData().getValue().getData().stream().filter(product -> product.getAttributes().getName().contains(filterString)).collect(Collectors.toList());
                    ProductResponse productFilteredSearchResponse = new ProductResponse(productFilteredSearch);
                    adapter.setProductResponse(productFilteredSearchResponse);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setAdapter() {
        binding.imageItem1.setAdapter(adapter);
        GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(),2);
        binding.imageItem1.setLayoutManager(layoutManagerProduct);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setObserverData() {
        viewModel.getProductResponseLiveData().observe(requireActivity(), productResponse -> {
            adapter.setProductResponse(new ProductResponse());
            adapter.notifyDataSetChanged();
        });
        viewModel.fetchData();
    }

    private void setUI() {
        binding.etSearch.requestFocus();
    }

    private void setIntitalData() {
        adapter = new ProductListFilterAdapter(new ProductResponse());
        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }
}