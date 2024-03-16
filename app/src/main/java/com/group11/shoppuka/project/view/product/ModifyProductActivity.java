package com.group11.shoppuka.project.view.product;

import static kotlin.io.ByteStreamsKt.readBytes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityModifyProductBinding;
import com.group11.shoppuka.project.model.category.Category;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductData;
import com.group11.shoppuka.project.model.product.ProductRequest;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.viewmodel.CategoryViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@AndroidEntryPoint
public class ModifyProductActivity extends AppCompatActivity {
    ActivityModifyProductBinding binding;
    CategoryViewModel cateViewModel;
    private final ProductData productData = new ProductData();
    @Override
    protected void onResume() {
        super.onResume();
    }
    String imageUrl = "";
    Intent data;
    Product currentProduct;

    CategoryResponse currentCategoryResponse;

    ArrayList<String> dataMenu;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setIntialData();
        setUI();
        setObserverData();
        setEventHandler();
    }

    private void setEventHandler() {
        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productData.setIdCategory(i+1);
                System.out.println("ID CATEGORY : "+ i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnUpdateAccount.setOnClickListener(view -> {
            if (binding.edtProductName.getText().toString().isEmpty()){
                binding.edtProductName.setError("Tên sản phẩm không được để trống");
            }
            else if (binding.edtProductPrice.getText().toString().isEmpty()){
                binding.edtProductPrice.setError("Giá sản phẩm không được để trống");
            }
            else if (binding.edtProductSalePrice.getText().toString().isEmpty()){
                binding.edtProductSalePrice.setError("Giá khuyến mãi không được để trống");
            }
            else if (Integer.parseInt(binding.edtProductPrice.getText().toString()) <= Integer.parseInt(binding.edtProductSalePrice.getText().toString())){
                binding.edtProductSalePrice.setError("Giá khuyến mãi không được lớn hơn giá gốc");
            }
            else if (binding.edtProductDescription.getText().toString().isEmpty()){
                binding.edtProductDescription.setError("Mô tả sản phẩm không được để trống");
            }
            else {
                if (productData.getImageURL() == null){
                    productData.setImageURL(currentProduct.getAttributes().getImageURL());
                }
                productData.setName(binding.edtProductName.getText().toString());
                productData.setPrice(Integer.parseInt(binding.edtProductPrice.getText().toString()));
                productData.setSalePrice(Integer.parseInt(binding.edtProductSalePrice.getText().toString()));
                productData.setDescription(binding.edtProductDescription.getText().toString());
                productData.setCountSearch(0);
                productData.setIdProduct(currentProduct.getId());
                ProductRequest productRequest = new ProductRequest();
                productRequest.setData(productData);
                ProductViewModel productViewModel = new ViewModelProvider(ModifyProductActivity.this).get(ProductViewModel.class);
                productViewModel.updateData(currentProduct.getId(),productRequest);
                Toast.makeText(getApplicationContext(),"Product được chỉnh sửa thành công !",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        binding.ivImageURL.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),MyApplication.PICK_IMAGE);
        });
    }

    private void setObserverData() {
        cateViewModel.getCategoryResponseLiveData().observe(ModifyProductActivity.this, categoryResponse -> {
            currentCategoryResponse.setData(categoryResponse.getData());
            for (Category category : currentCategoryResponse.getData()){
                dataMenu.add(category.getAttributes().getName());
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spnCategory.setAdapter(adapter);
            binding.spnCategory.setSelection(currentProduct.getAttributes().getIdCategory()-1);

        });



    }

    private void setUI() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(currentProduct.getAttributes().getName());
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
        String url = MyApplication.localHost + currentProduct.getAttributes().getImageURL();
        Glide.with(this).load(url).into(binding.ivImageURL);
        binding.edtProductName.setText(currentProduct.getAttributes().getName());
        binding.edtProductPrice.setText(String.valueOf(currentProduct.getAttributes().getPrice()));
        binding.edtProductSalePrice.setText(String.valueOf(currentProduct.getAttributes().getSalePrice()));
        productData.setIdCategory(currentProduct.getAttributes().getIdCategory());
        binding.edtProductDescription.setText(currentProduct.getAttributes().getDescription());
    }

    private void setIntialData() {
        data = getIntent();
        currentProduct = (Product) data.getSerializableExtra("product");
        currentCategoryResponse = new CategoryResponse();
        dataMenu = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataMenu);
        cateViewModel =  new ViewModelProvider(this).get(CategoryViewModel.class);
        cateViewModel.fetchDataCategory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyApplication.PICK_IMAGE){
            if (resultCode == RESULT_OK){
                Uri imageUri = null;
                if (data != null) {
                    imageUri = data.getData();
                }
                binding.ivImageURL.setImageURI(imageUri);
                try {
                    InputStream inputStream =getContentResolver().openInputStream(imageUri);
                    byte[] imageData = readBytes(inputStream);
                    RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)),imageData);
                    MultipartBody.Part image = MultipartBody.Part.createFormData("files","image.jpg",requestFile);
                    RetrofitService retrofitService = new RetrofitService();
                    ApiService apiService = retrofitService.retrofit.create(ApiService.class);

                    apiService.uploadImage(image).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                            if (response.isSuccessful()) {
                                try {
                                    assert response.body() != null;
                                    String responseBody = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseBody);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    imageUrl = jsonObject.getString("url");
                                    productData.setImageURL(imageUrl);
                                    System.out.println(imageUrl);
                                }  catch (JSONException | IOException e) {
                                    throw new RuntimeException(e);

                                }

                                System.out.println("upload hình thành công");
                            } else {
                                int statusCode = response.code();
                                ResponseBody errorBody = response.errorBody();
                                String errorMessage = null;
                                try {
                                    if (response.errorBody() != null) {
                                        errorMessage = response.errorBody().string();
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(statusCode);
                                System.out.println(errorMessage);
                                try {
                                    errorMessage = errorBody != null ? errorBody.string() : "";
                                    System.out.println(errorMessage);
                                } catch (IOException e) {
                                    Toast.makeText(ModifyProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }



        }
    }
}