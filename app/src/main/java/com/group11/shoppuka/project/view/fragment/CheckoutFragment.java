package com.group11.shoppuka.project.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.common.util.IOUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.group11.shoppuka.CartActivity;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.FragmentCheckoutBinding;
import com.group11.shoppuka.project.adapter.CheckoutItemAdapter;
import com.group11.shoppuka.project.model.Order;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.order.OrderData;
import com.group11.shoppuka.project.model.order.OrderRequest;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.viewmodel.CartViewModel;
import com.group11.shoppuka.project.viewmodel.OrderViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import org.checkerframework.checker.units.qual.C;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class CheckoutFragment extends Fragment {



    private RecyclerView mRecyclerView;
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Checkout");
        productViewModel.fetchData();
        cartViewModel.fetchListCart(getContext());
    }
    String selectedDic;


    private void setupSpinnerDis(String [] Dis,Spinner spinnerDic) {
        // Khởi tạo adapter và thiết lập dữ liệu
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Dis);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDic.setAdapter((SpinnerAdapter) adapter);
        spinnerDic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDic = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
            // Xử lý khi một tỉnh được chọn
        });
    }
    private FragmentCheckoutBinding binding;

    private CheckoutItemAdapter checkoutItemAdapter;

    private CartViewModel cartViewModel;

    private ProductViewModel productViewModel;

    private OrderViewModel orderViewModel;

    private OrderData orderData = new OrderData();

    private String selectedAddress;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCheckoutBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        mRecyclerView = view.findViewById(R.id.recyclerViewCart);

//        Log.d("TEST1", String.valueOf(ListOrder.size()));
//        setupRecyclerView(ListOrder);
        //

        String[] provinceNames = {"Hà Nội", "TP Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ","Vĩnh Long"};
        Spinner spinnerProvince = view.findViewById(R.id.spinner_province);
        Spinner spinnerDic= view.findViewById(R.id.spinner_district);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, provinceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAddress = (String) parent.getItemAtPosition(position);
                switch (position){
                    case 0: if(position==0){
                        String[] provinceDic = {"Ba Đình", "Hoàn Kiếm", "Hai Bà Trưng", "Đống Đa", "Tây Hồ", "Cầu Giấy", "Thanh Xuân", "Hoàng Mai", "Long Biên", "Bắc Từ Liêm", "Nam Từ Liêm", "Hà Đông", "Sơn Tây", "Ba Vì", "Phúc Thọ", "Đan Phượng", "Hoài Đức", "Quốc Oai", "Thạch Thất", "Chương Mỹ", "Thanh Oai", "Thường Tín", "Phú Xuyên", "Ứng Hòa", "Mỹ Đức"};
                        setupSpinnerDis(provinceDic,spinnerDic);
                    }break;
                    case 1: if(position==1){
                        String[] provinceDic = {"Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12", "Quận Bình Tân", "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình", "Quận Tân Phú", "Quận Thủ Đức", "Huyện Bình Chánh", "Huyện Cần Giờ", "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè"};
                        setupSpinnerDis(provinceDic,spinnerDic);
                    }break;
                    case 2: if(position==2) {
                        String[] provinceDic = {"Quận Hải Châu", "Quận Thanh Khê", "Quận Sơn Trà", "Quận Ngũ Hành Sơn", "Quận Liên Chiểu", "Quận Cẩm Lệ", "Huyện Hòa Vang"};
                        setupSpinnerDis(provinceDic,spinnerDic);

                    }break;
                    case 3: if(position==3) {
                        String[] provinceDic = {"Quận Hồng Bàng", "Quận Lê Chân", "Quận Ngô Quyền", "Quận Kiến An", "Quận Hải An", "Quận Đồ Sơn", "Huyện An Dương", "Huyện An Lão", "Huyện Kiến Thuỵ", "Huyện Tiên Lãng", "Huyện Vĩnh Bảo", "Huyện Thủy Nguyên", "Huyện Cát Hải", "Huyện Bạch Long Vĩ"};
                        setupSpinnerDis(provinceDic,spinnerDic);
                    }break;
                    case 4: if(position==4) {
                        String[] provinceDic = {"Quận Ninh Kiều", "Quận Bình Thủy", "Quận Cái Răng", "Quận Ô Môn", "Quận Thốt Nốt", "Huyện Vĩnh Thạnh", "Huyện Cờ Đỏ", "Huyện Phong Điền", "Huyện Thới Lai"};
                        setupSpinnerDis(provinceDic,spinnerDic);
                    }break;
                    case 5: if(position==5) {
                        String[] provinceDic = {"Thành phố Vĩnh Long", "Huyện Long Hồ", "Huyện Mang Thít" ,"Huyện Vũng Liêm" ,"Huyện Tam Bình" ,"Thị xã Bình Minh" ,"Huyện Trà Ôn" ,"Huyện Bình Tân"};
                        setupSpinnerDis(provinceDic,spinnerDic);
                    }break;

                    default: break;
                }

                // Xử lý khi một tỉnh được chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có tỉnh nào được chọn
            }
        });

        Intent intent = getActivity().getIntent();
        CartResponse intentCart = (CartResponse) intent.getSerializableExtra(MyApplication.KEY_GET_LISTCART);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyApplication.KEY_LOGIN, Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(MyApplication.KEY_ACCOUNT_PHONE,null);
        binding.tvPhoneNumber.setText(phoneNumber);
        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        orderViewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);

        checkoutItemAdapter = new CheckoutItemAdapter(new CartResponse(), new ProductResponse());
        productViewModel.getProductResponseLiveData().observe(getActivity(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                checkoutItemAdapter.setProductResponse(productResponse);
                checkoutItemAdapter.setCartResponse(intentCart);
                int price = 0;
                for (Cart cart : intentCart.getData()) price += (cart.getAttributes().getTotalPrice() * cart.getAttributes().getCount());
                binding.totalPrice.setText(String.valueOf(price) + " VNĐ");
                checkoutItemAdapter.notifyDataSetChanged();
            }
        });

        binding.recyclerViewCart.setAdapter(checkoutItemAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewCart.setLayoutManager(linearLayoutManager);


        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        binding.btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etStreet.getText().toString().isEmpty()){
                    binding.etStreet.setError("Không để trống tên đường !!");
                }
                else {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    for (Cart cart : intentCart.getData()){
                        UUID uuid = UUID.randomUUID();
                        int count = cart.getAttributes().getCount();
                        OrderData currentOrderData = new OrderData();
                        OrderRequest currentOrderRequest = new OrderRequest();
                        for (int id : cart.getAttributes().getIdResource()){
                            cartViewModel.deleteIdCart(id);
                        }
                        currentOrderData.setIdProduct(cart.getAttributes().getIdProduct());
                        currentOrderData.setQuantity(count);
                        currentOrderData.setTotalPrice(cart.getAttributes().getTotalPrice() * count);
                        currentOrderData.setStatus(0);
                        currentOrderData.setPhoneNumber(phoneNumber);
                        currentOrderData.setAddress(selectedAddress + ", " + selectedDic + ", " + binding.etStreet.getText().toString());
                        currentOrderRequest.setData(currentOrderData);
                        currentOrderData.setDateCreate(formatter.format(date));
                        currentOrderData.setOrderCode(uuid.toString());
                        orderViewModel.createOrder(currentOrderRequest);
                        cartViewModel.deleteIdCart(cart.getId());



                    }
                    Toast.makeText(getActivity().getApplicationContext(), "Thanh toán thành công",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });

        return view;
    }


    //  Tính toán tổng tiền thanh toán
    private double calculateTotal(List<Order> orderList) {
        double result = 0;
        for (int i = 0; i < orderList.size(); i++) {
            result += orderList.get(i).getTotalPrice();
        }
        return result;
    }

    public class City {
        public String name;
        public List<District> districts;

        public String getName() {
            return name;
        }

        public List<District> getDistricts() {
            return districts;
        }
    }

    public class District {
        public String name;

        public String getName() {
            return name;
        }
    }
}
