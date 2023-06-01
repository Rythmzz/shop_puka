package com.group11.shoppuka.project.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.project.view.CheckoutActivity;
import com.group11.shoppuka.databinding.FragmentCartPageBinding;
import com.group11.shoppuka.project.adapter.CartListAdapter;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.viewmodel.CartViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartPageFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FragmentCartPageBinding binding;

    private CartListAdapter adapter;

    private CartViewModel cartViewModel;
    private ProductViewModel productViewModel;

    private ProductResponse currentProductResponse;
    private CartResponse currentCartResponse;

    @Override
    public void onResume() {
        super.onResume();
        productViewModel.fetchData();
        cartViewModel.fetchListCart(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);

        adapter = new CartListAdapter(new CartResponse(), new ProductResponse());
        binding.recyclerViewCart.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewCart.setLayoutManager(layoutManager);


        cartViewModel.getCartResponseMutableLiveData().observe(getActivity(), new Observer<CartResponse>() {
            @Override
            public void onChanged(CartResponse cartResponse) {
                if (cartResponse.getData().size() == 0 ) binding.csNotCart.setVisibility(View.VISIBLE);
                else binding.csNotCart.setVisibility(View.GONE);
                adapter.setCartResponse(cartResponse);
                int totalPrice = 0;
                currentCartResponse = new CartResponse();
                currentCartResponse.setData(cartResponse.getData());
                for (Cart cart : cartResponse.getData()) totalPrice+= (cart.getAttributes().getTotalPrice() * cart.getAttributes().getCount());
                binding.totalPrice.setText(String.valueOf(totalPrice) + " VNĐ");
                adapter.notifyDataSetChanged();
            }
        });

        productViewModel.getProductResponseLiveData().observe(getActivity(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                adapter.setProductResponse(productResponse);
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CheckoutActivity.class);
                intent.putExtra(MyApplication.KEY_GET_LISTCART,currentCartResponse);
                view.getContext().startActivity(intent);
            }
        });

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}