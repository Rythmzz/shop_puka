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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderPageFragment newInstance(String param1, String param2) {
        OrderPageFragment fragment = new OrderPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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