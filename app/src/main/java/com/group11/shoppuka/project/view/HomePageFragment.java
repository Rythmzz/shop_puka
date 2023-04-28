package com.group11.shoppuka.project.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.FragmentHomePageBinding;
import com.group11.shoppuka.project.adapter.CategoryAdapter;
import com.group11.shoppuka.project.adapter.ProductListAdapter;
import com.group11.shoppuka.project.adapter.SliderAdapter;
import com.group11.shoppuka.project.model.Product;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.xmlpull.v1.XmlPullParser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private FragmentHomePageBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    SliderView sliderView;
    int[] images = {
            R.drawable.pk_slider_1,
            R.drawable.pk_slider_2,
            R.drawable.pk_slider_3,
            R.drawable.pk_slider_4,
            R.drawable.pk_slider_5
    };
    int[] imagesCategory = {
            R.drawable.pk_cate_clothes,
            R.drawable.pk_cate_houseware,
            R.drawable.pk_cate_jewery,
            R.drawable.pk_cate_makeup,
            R.drawable.pk_cate_technology,
            R.drawable.pk_cate_furniture
    };
    String[] textCategory ={
            "Quần Áo",
            "Gia Dụng",
            "Trang Sức",
            "Mỹ Phẩm",
            "Công Nghệ",
            "Nội Thất"
    };

    Product[] listProduct ={
            new Product("Áo thun màu trơn Cơ bản","114.000VNĐ",R.drawable.pk_product_aothun),
            new Product("EMERY ROSE Áo thun Gân đan màu trơn Giải trí","201.000VNĐ",R.drawable.pk_product_aothun1),
            new Product("Sofa giường thông minh ZD120A","12.500.000VNĐ",R.drawable.pk_product_sofa),
            new Product("Bộ khay trà tráng men cao cấp 43x26cm 0096","680.000VNĐ",R.drawable.pk_product_khaytra),
            new Product("KỆ DAO THỚT ĐA NĂNG","280.000VNĐ",R.drawable.pk_product_kedaothot),
            new Product("Laptop Gaming MSI Raider GE78 HX 13VH-076VN","118.990.000VNĐ",R.drawable.pk_product_lapge78),
            new Product("Dây chuyền bạc nữ khắc tên hình trái tim DCN0620","479.000VNĐ",R.drawable.pk_product_vongtay)
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomePageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        sliderView = binding.imageSlider;

        SliderAdapter adapter = new SliderAdapter(images);

        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.startAutoCycle();
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

        CategoryAdapter categoryAdapter = new CategoryAdapter(imagesCategory,textCategory);

        binding.imageItem.setAdapter(categoryAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.imageItem.setLayoutManager(layoutManager);

        ProductListAdapter listProductAdapter = new ProductListAdapter(listProduct);

        binding.imageItem1.setAdapter(listProductAdapter);
        LinearLayoutManager layoutManagerProduct = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);

        binding.imageItem1.setLayoutManager(layoutManagerProduct);

        binding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean b) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment newFragment = new SearchPageFragment();
                fragmentTransaction.replace(R.id.frame_layout,newFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}