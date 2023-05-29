package com.group11.shoppuka.project.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.databinding.FragmentCartPageBinding;
import com.group11.shoppuka.project.adapter.CartListAdapter;
import com.group11.shoppuka.project.model.account.UserData;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.viewmodel.CartViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartPageFragment newInstance(String param1, String param2) {
        CartPageFragment fragment = new CartPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);

        productViewModel.fetchData();
        cartViewModel.fetchListCart(getActivity());

        adapter = new CartListAdapter(new CartResponse(), new ProductResponse());
        binding.recyclerViewCart.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewCart.setLayoutManager(layoutManager);


        cartViewModel.getCartResponseMutableLiveData().observe(getActivity(), new Observer<CartResponse>() {
            @Override
            public void onChanged(CartResponse cartResponse) {
                adapter.setCartResponse(cartResponse);
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

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}