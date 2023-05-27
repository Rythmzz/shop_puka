package com.group11.shoppuka.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.group11.shoppuka.CheckoutActivity;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.adapter.CheckoutItemAdapter;
import com.group11.shoppuka.project.model.Order;

import org.json.JSONException;

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
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;


public class CheckoutFragment extends Fragment {
    private Context mContext;
    private Button mButtonConfirm, mButtonCancel;
    private TextView mTextTotal;
    private EditText editPhone,editStreet;
    List<Order> ListOrder ;
    // Contructors
    public CheckoutFragment(List<Order> listOrder) {
        this.ListOrder = listOrder;
        Log.d("TEST2", String.valueOf(ListOrder.size())+" + "+ListOrder.get(0).getProductName());
    }

    private RecyclerView mRecyclerView;
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Checkout");
    }

    private void setupRecyclerView(List<Order> orderList) {
        // Khởi tạo adapter và thiết lập dữ liệu
        CheckoutItemAdapter adapter = new CheckoutItemAdapter(orderList, getContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void setupSpinnerDis(String [] Dis,Spinner spinnerDic) {
        // Khởi tạo adapter và thiết lập dữ liệu
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Dis);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDic.setAdapter((SpinnerAdapter) adapter);
        spinnerDic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDic = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
            // Xử lý khi một tỉnh được chọn
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        super.onCreate(savedInstanceState);
        mRecyclerView = view.findViewById(R.id.checkout_items_container);
        Log.d("TEST1", String.valueOf(ListOrder.size()));
        setupRecyclerView(ListOrder);
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
                String selectedProvince = (String) parent.getItemAtPosition(position);
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

        //
        mButtonCancel = (Button) view.findViewById(R.id.checkout_cancel);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                startActivity(new Intent(getContext(), CartActivity.class));
            }
        });
        editPhone = view.findViewById(R.id.editPhone);
        editStreet = view.findViewById(R.id.editStreet);
        mButtonConfirm = (Button) view.findViewById(R.id.checkout_pay);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPhone.getText().toString().isEmpty()){
                   // Toast.makeText(mContext.getApplicationContext(),"Vui lòng không để trống số điện thoại!",Toast.LENGTH_LONG);
                    editPhone.setError("Vui lòng không để trống số điện thoại!");
                }
                else if (!editPhone.getText().toString().matches("^0\\d{9}$")) {
                    editPhone.setError("Vui lòng nhập số điện thoại hợp lệ!");}
                else if (editStreet.getText().toString().isEmpty()){
                    //Toast.makeText(mContext.getApplicationContext(),"Vui lòng không để trống địa chỉ!",Toast.LENGTH_LONG);
                    editStreet.setError("Vui lòng không để trống địa chỉ!");
                }
                else
                {
                        getActivity().finish();
                        Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), CartActivity.class));
                }
            }
        });

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(0);
        nf.setCurrency(Currency.getInstance("VND"));
        String formattedPrice = nf.format((calculateTotal(ListOrder)));
        mTextTotal = (TextView) view.findViewById(R.id.checkout_total);
        mTextTotal.setText(formattedPrice);
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
