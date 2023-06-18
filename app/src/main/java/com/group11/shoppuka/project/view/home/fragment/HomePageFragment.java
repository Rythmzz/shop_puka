package com.group11.shoppuka.project.view.home.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.FragmentHomePageBinding;
import com.group11.shoppuka.project.adapter.CategoryAdapter;
import com.group11.shoppuka.project.adapter.ProductListAllAdapter;
import com.group11.shoppuka.project.adapter.ProductListSalePriceAdapter;
import com.group11.shoppuka.project.adapter.ProductListTopSearchAdapter;
import com.group11.shoppuka.project.adapter.SliderAdapter;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.viewmodel.CategoryViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomePageFragment extends Fragment  {

    public interface OnItemSelectedListener {
        void onItemSelected(int itemID);
    }
    SliderView sliderView;
    int[] images;

    FragmentHomePageBinding binding;
    private CategoryViewModel viewModelCategory;
    private ProductViewModel viewModelProduct;
    private CategoryAdapter categoryAdapter;
    private SliderAdapter sliderAdapter;
    private ProductListSalePriceAdapter productListSalePriceAdapter;
    private ProductListTopSearchAdapter productListTopSearchAdapter;
    private ProductListAllAdapter productListAllAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomePageBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        setIntialData();
        setAdapter();
        setObserverData();
        setFetchData();
        setEventHandler();
        return view;
    }
    private void setIntialData() {

        images = new int[]{
                R.drawable.pk_slider_1,
                R.drawable.pk_slider_2,
                R.drawable.pk_slider_3,
                R.drawable.pk_slider_4,
                R.drawable.pk_slider_5
        };

        sliderView = binding.imageSlider;

        viewModelCategory = new ViewModelProvider(this).get(CategoryViewModel.class);
        viewModelProduct = new ViewModelProvider(this).get(ProductViewModel.class);

        sliderAdapter = new SliderAdapter(images);
        categoryAdapter = new CategoryAdapter(new CategoryResponse());
        productListSalePriceAdapter = new ProductListSalePriceAdapter(new ProductResponse());
        productListTopSearchAdapter = new ProductListTopSearchAdapter(new ProductResponse());
        productListAllAdapter = new ProductListAllAdapter(new ProductResponse());

    }
    private void setAdapter() {

        // Set Silder Adapter

        binding.imageSlider.setSliderAdapter(sliderAdapter);
        binding.imageSlider.startAutoCycle();
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        // Set Category Adapter

        binding.imageItem.setAdapter(categoryAdapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.imageItem.setLayoutManager(layoutManager);

        // Set Product Adapter
        LinearLayoutManager layoutManagerProduct = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutManagerProduct1 = new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        GridLayoutManager layoutManagerProduct2 = new GridLayoutManager(this.getContext(),2);

        binding.imageItem1.setAdapter(productListSalePriceAdapter);
        binding.imageItem1.setLayoutManager(layoutManagerProduct);


        binding.imageItem2.setAdapter(productListTopSearchAdapter);
        binding.imageItem2.setLayoutManager(layoutManagerProduct1);

        binding.imageItem3.setAdapter(productListAllAdapter);
        binding.imageItem3.setLayoutManager(layoutManagerProduct2);
    }
    private void setObserverData() {
        // Observer Category
        viewModelCategory.getCategoryResponseLiveData().observe(getViewLifecycleOwner(), new Observer<CategoryResponse>() {
            @Override
            public void onChanged(CategoryResponse categoryResponse) {
                categoryAdapter.setCategoryResponseList(categoryResponse);
                categoryAdapter.notifyDataSetChanged();
            }
        });

        // Observer General Product
        viewModelProduct.getProductResponseLiveData().observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse productResponse) {
                List<Product> filterSalePriceProduct = new ArrayList<>();
                List<Product> allProduct = viewModelProduct.getProductResponseLiveData().getValue().getData();
                int count = 0;
                for (Product product : viewModelProduct.getProductResponseLiveData().getValue().getData()) {
                    if (product.getAttributes().getSalePrice() != 0 && count <= 5) {
                        filterSalePriceProduct.add(product);
                        count++;
                    }
                }
                List<Product> filterTopSearchProductCopy = new ArrayList<>(allProduct);
                for ( int i = 0 ; i < filterTopSearchProductCopy.size() ; i++){
                    for (int j = i + 1 ; j < filterTopSearchProductCopy.size(); j++){
                        if (filterTopSearchProductCopy.get(i).getAttributes().getCountSearch() > filterTopSearchProductCopy.get(j).getAttributes().getCountSearch()){
                            Product temp = filterTopSearchProductCopy.get(i);
                            filterTopSearchProductCopy.set(i,filterTopSearchProductCopy.get(j));
                            filterTopSearchProductCopy.set(j,temp);
                        }
                    }
                }
                ProductResponse allProductResponse = new ProductResponse(allProduct);
                ProductResponse filteredSalePriceProductResponse = new ProductResponse(filterSalePriceProduct);
                ProductResponse filteredProductTopSearchResponse = new ProductResponse(filterTopSearchProductCopy.subList(0, Math.min(6, filterTopSearchProductCopy.size())));
                productListSalePriceAdapter.setProductResponse(filteredSalePriceProductResponse);
                productListSalePriceAdapter.notifyDataSetChanged();
                productListTopSearchAdapter.setProductResponse(filteredProductTopSearchResponse);
                productListTopSearchAdapter.notifyDataSetChanged();
                productListAllAdapter.setProductResponse(allProductResponse);
                productListAllAdapter.notifyDataSetChanged();
            }
        });

    }
    private void setFetchData() {
        viewModelCategory.fetchDataCategory();
        viewModelProduct.fetchData();
    }
    private void setEventHandler() {
        binding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean b) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment newFragment = new SearchPageFragment();
                fragmentTransaction.replace(R.id.frame_layout,newFragment);
                fragmentTransaction.commit();
                if (getActivity() instanceof OnItemSelectedListener) {
                    ((OnItemSelectedListener) getActivity()).onItemSelected(R.id.Search);
                }

            }
        });
    }
}