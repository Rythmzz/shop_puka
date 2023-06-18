package com.group11.shoppuka.project.view.home.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.group11.shoppuka.project.other.MyApplication;
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
//        cartViewModel.fetchListCart(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        intialData();
        setAdapter();
        setObserverData();
        setEventHandler();
        return view;
    }

    private void setEventHandler() {
        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CheckoutPageActivity.class);
                intent.putExtra(MyApplication.KEY_GET_LISTCART,currentCartResponse);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void setObserverData() {
        cartViewModel.getCartResponseMutableLiveData().observe(getActivity(), new Observer<CartResponse>() {
            @Override
            public void onChanged(CartResponse cartResponse) {
                if (cartResponse.getData().size() == 0 ) binding.csNotCart.setVisibility(View.VISIBLE);
                else binding.csNotCart.setVisibility(View.GONE);
                cartListAdapter.setCartResponse(cartResponse);
                int totalPrice = 0;
                currentCartResponse = new CartResponse();
                currentCartResponse.setData(cartResponse.getData());
                for (Cart cart : cartResponse.getData()) totalPrice+= (cart.getAttributes().getTotalPrice() * cart.getAttributes().getCount());
                binding.totalPrice.setText(String.valueOf(totalPrice) + " VNĐ");
                cartListAdapter.notifyDataSetChanged();
            }
        });
        cartViewModel.fetchListCart(getActivity());

        productViewModel.getProductResponseLiveData().observe(getActivity(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                cartListAdapter.setProductResponse(productResponse);
                cartListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setAdapter() {
        cartListAdapter = new CartListAdapter(new CartResponse(), new ProductResponse(),getActivity());
        binding.recyclerViewCart.setAdapter(cartListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewCart.setLayoutManager(layoutManager);


    }

    private void intialData() {
        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);


    }

}