package com.group11.shoppuka.project.view;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.FragmentSearchPageBinding;
import com.group11.shoppuka.project.adapter.ProductListAllAdapter;
import com.group11.shoppuka.project.adapter.ProductListFilterAdapter;
import com.group11.shoppuka.project.model.ProductTest;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    List<ProductTest> listProduct = Arrays.asList(new ProductTest("Áo thun màu trơn Cơ bản","114.000VNĐ",R.drawable.pk_product_aothun),
            new ProductTest("EMERY ROSE Áo thun Gân đan màu trơn Giải trí","201.000VNĐ",R.drawable.pk_product_aothun1),
            new ProductTest("Sofa giường thông minh ZD120A","12.500.000VNĐ",R.drawable.pk_product_sofa),
            new ProductTest("Bộ khay trà tráng men cao cấp 43x26cm 0096","680.000VNĐ",R.drawable.pk_product_khaytra),
            new ProductTest("KỆ DAO THỚT ĐA NĂNG","280.000VNĐ",R.drawable.pk_product_kedaothot),
            new ProductTest("Laptop Gaming MSI Raider GE78 HX 13VH-076VN","118.990.000VNĐ",R.drawable.pk_product_lapge78),
            new ProductTest("Dây chuyền bạc nữ khắc tên hình trái tim DCN0620","479.000VNĐ",R.drawable.pk_product_vongtay));

    public static SearchPageFragment newInstance(String param1, String param2) {
        SearchPageFragment fragment = new SearchPageFragment();
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
    private FragmentSearchPageBinding binding;

    private ProductListFilterAdapter adapter;

    private ProductViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchPageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.etSearch.requestFocus();
        adapter = new ProductListFilterAdapter(new ProductResponse());
        viewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        viewModel.getProductResponseLiveData().observe(getActivity(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                adapter.setProductResponse(new ProductResponse());
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.fetchData();


        binding.imageItem1.setAdapter(adapter);
        GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(),2);

        binding.imageItem1.setLayoutManager(layoutManagerProduct);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filterString = s.toString();
                if(s.toString().isEmpty()){
                    binding.tvSearch.setText("");
                    adapter.setProductResponse(new ProductResponse());
                    adapter.notifyDataSetChanged();
                }
                else {
                    binding.tvSearch.setText("Search Result For Keyword \""+s.toString()+"\"");
                    List<ProductTest> filteredProducts =  listProduct.stream().filter(product -> product.getName().contains(filterString)).collect(Collectors.toList());
                    List<Product> productFilteredSearch = viewModel.getProductResponseLiveData().getValue().getData().stream().filter(product -> product.getAttributes().getName().contains(filterString)).collect(Collectors.toList());
                    ProductResponse productFilteredSearchResponse = new ProductResponse(productFilteredSearch);
                    adapter.setProductResponse(productFilteredSearchResponse);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return view;
    }
}