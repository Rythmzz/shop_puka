package com.group11.shoppuka.project.view.checkout.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.group11.shoppuka.databinding.FragmentCheckoutBinding;
import com.group11.shoppuka.project.adapter.CheckoutItemAdapter;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.order.OrderData;
import com.group11.shoppuka.project.model.order.OrderRequest;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.viewmodel.CartViewModel;
import com.group11.shoppuka.project.viewmodel.OrderViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class CheckoutFragment extends Fragment {

    private String selectedDic;

    private String selectedAddress;

    private String[] provinceNames;

    private FragmentCheckoutBinding binding;

    private CheckoutItemAdapter checkoutItemAdapter;

    private CartViewModel cartViewModel;
    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;
    @Inject
    SharedPreferences sharedPreferences;

    private CartResponse dataCart;

    private String phoneNumber;

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Thanh Toán");
        productViewModel.fetchData();
        cartViewModel.fetchListCart();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentCheckoutBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        setIntialData();
        setAdapter();
        setEventHandler();
        setUI();
        setObserverData();
        return view;
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void setObserverData() {
        productViewModel.getProductResponseLiveData().observe(requireActivity(), productResponse -> {
            checkoutItemAdapter.setProductResponse(productResponse);
            checkoutItemAdapter.setCartResponse(dataCart);
            int price = 0;
            for (Cart cart : dataCart.getData()) price += (cart.getAttributes().getTotalPrice() * cart.getAttributes().getCount());
            binding.totalPrice.setText(MyApplication.formatCurrency(String.valueOf(price)) + " VNĐ");
            checkoutItemAdapter.notifyDataSetChanged();
        });
    }

    private void setUI() {
        binding.tvPhoneNumber.setText(phoneNumber);
    }

    private void setEventHandler() {
        binding.spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAddress = (String) parent.getItemAtPosition(position);
                switch (position){
                    case 0: {
                        String[] provinceDic = {"Ba Đình", "Hoàn Kiếm", "Hai Bà Trưng", "Đống Đa", "Tây Hồ", "Cầu Giấy", "Thanh Xuân", "Hoàng Mai", "Long Biên", "Bắc Từ Liêm", "Nam Từ Liêm", "Hà Đông", "Sơn Tây", "Ba Vì", "Phúc Thọ", "Đan Phượng", "Hoài Đức", "Quốc Oai", "Thạch Thất", "Chương Mỹ", "Thanh Oai", "Thường Tín", "Phú Xuyên", "Ứng Hòa", "Mỹ Đức"};
                        setupSpinnerDis(provinceDic,binding.spinnerDistrict);
                    }
                    break;
                    case 1: {
                        String[] provinceDic = {"Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12", "Quận Bình Tân", "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình", "Quận Tân Phú", "Quận Thủ Đức", "Huyện Bình Chánh", "Huyện Cần Giờ", "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè"};
                        setupSpinnerDis(provinceDic,binding.spinnerDistrict);
                    }
                    break;
                    case 2: {
                        String[] provinceDic = {"Quận Hải Châu", "Quận Thanh Khê", "Quận Sơn Trà", "Quận Ngũ Hành Sơn", "Quận Liên Chiểu", "Quận Cẩm Lệ", "Huyện Hòa Vang"};
                        setupSpinnerDis(provinceDic,binding.spinnerDistrict);

                    }
                    break;
                    case 3: {
                        String[] provinceDic = {"Quận Hồng Bàng", "Quận Lê Chân", "Quận Ngô Quyền", "Quận Kiến An", "Quận Hải An", "Quận Đồ Sơn", "Huyện An Dương", "Huyện An Lão", "Huyện Kiến Thuỵ", "Huyện Tiên Lãng", "Huyện Vĩnh Bảo", "Huyện Thủy Nguyên", "Huyện Cát Hải", "Huyện Bạch Long Vĩ"};
                        setupSpinnerDis(provinceDic,binding.spinnerDistrict);
                    }
                    break;
                    case 4: {
                        String[] provinceDic = {"Quận Ninh Kiều", "Quận Bình Thủy", "Quận Cái Răng", "Quận Ô Môn", "Quận Thốt Nốt", "Huyện Vĩnh Thạnh", "Huyện Cờ Đỏ", "Huyện Phong Điền", "Huyện Thới Lai"};
                        setupSpinnerDis(provinceDic,binding.spinnerDistrict);
                    }
                    break;
                    case 5:
                        String[] provinceDic = {"Thành phố Vĩnh Long", "Huyện Long Hồ", "Huyện Mang Thít" ,"Huyện Vũng Liêm" ,"Huyện Tam Bình" ,"Thị xã Bình Minh" ,"Huyện Trà Ôn" ,"Huyện Bình Tân"};
                        setupSpinnerDis(provinceDic,binding.spinnerDistrict);
                        break;

                    default: break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.btnCancel.setOnClickListener(view -> requireActivity().finish());

        binding.btnPaypal.setOnClickListener(view -> {
            if (binding.etStreet.getText().toString().isEmpty()){
                binding.etStreet.setError("Không để trống tên đường !!");
            }
            else {
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                for (Cart cart : dataCart.getData()){
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
                Toast.makeText(requireActivity(), "Thanh toán thành công",Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        });


    }

    private void setAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, provinceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerProvince.setAdapter(adapter);

        binding.recyclerViewCart.setAdapter(checkoutItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewCart.setLayoutManager(linearLayoutManager);
    }

    private void setIntialData() {
        provinceNames = new String[]{"Hà Nội", "TP Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ", "Vĩnh Long"};

        Intent data = requireActivity().getIntent();
        dataCart = (CartResponse) data.getSerializableExtra(MyApplication.KEY_GET_LISTCART);
        phoneNumber = sharedPreferences.getString(MyApplication.KEY_ACCOUNT_PHONE,null);

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);

        checkoutItemAdapter = new CheckoutItemAdapter(new CartResponse(), new ProductResponse());

    }

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


}
