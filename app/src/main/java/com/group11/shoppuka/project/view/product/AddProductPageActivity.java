package com.group11.shoppuka.project.view.product;

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

import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityAddProductBinding;
import com.group11.shoppuka.project.model.category.Category;
import com.group11.shoppuka.project.model.category.CategoryResponse;
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

import java.io.ByteArrayOutputStream;
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
public class AddProductPageActivity extends AppCompatActivity {
    String imageUrl = "";
    CategoryViewModel cateViewModel;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private final ProductData productData = new ProductData();
    ProductViewModel productViewModel;
    ActivityAddProductBinding binding;

    @Override
    protected void onResume() {
        super.onResume();
        productViewModel.fetchData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUI();
        setIntialData();
        setAdapter();
        setEventHandler();
    }

    private void setIntialData() {
        cateViewModel = new ViewModelProvider(AddProductPageActivity.this).get(CategoryViewModel.class);
        productViewModel = new ViewModelProvider(AddProductPageActivity.this).get(ProductViewModel.class);
    }

    private void setUI() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tạo Sản Phẩm");
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
    }

    private void setAdapter() {
        cateViewModel.fetchDataCategory();
        CategoryResponse currentCategoryResponse = new CategoryResponse();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,data);
        cateViewModel.getCategoryResponseLiveData().observe(AddProductPageActivity.this, categoryResponse -> {
            currentCategoryResponse.setData(categoryResponse.getData());
            for (Category category : currentCategoryResponse.getData()){
                data.add(category.getAttributes().getName());
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spnCategory.setAdapter(adapter);
        });

        productViewModel.getProductResponseLiveData().observe(AddProductPageActivity.this, productResponse -> productData.setIdProduct(productResponse.getData().size()+1));
    }

    private void setEventHandler(){


        binding.btnSubmitAccount.setOnClickListener(v -> {
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
            else if (productData.getImageURL() == null){
                Toast.makeText(AddProductPageActivity.this,"Vui lòng chọn hình cho sản phẩm",Toast.LENGTH_SHORT).show();
            }
            else {
                productData.setName(binding.edtProductName.getText().toString());
                productData.setPrice(Integer.parseInt(binding.edtProductPrice.getText().toString()));
                productData.setSalePrice(Integer.parseInt(binding.edtProductSalePrice.getText().toString()));
                productData.setDescription(binding.edtProductDescription.getText().toString());
                productData.setCountSearch(0);
                ProductRequest productRequest = new ProductRequest();
                productRequest.setData(productData);
                productViewModel.createNewProduct(productRequest);
                Toast.makeText(AddProductPageActivity.this,"Product được tạo thành công !",Toast.LENGTH_SHORT).show();
                finish();


            }

        });

        binding.ivImageURL.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), MyApplication.PICK_IMAGE);
        });

        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productData.setIdCategory(i+1);
                System.out.println(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyApplication.PICK_IMAGE){
            if (resultCode == RESULT_OK){
                Uri imageUri = Objects.requireNonNull(data).getData();
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
                                    String responseBody = Objects.requireNonNull(response.body()).string();
                                    JSONArray jsonArray = new JSONArray(responseBody);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    imageUrl = jsonObject.getString("url");
                                    productData.setImageURL(imageUrl);
                                    System.out.println(imageUrl);
                                    // Sử dụng imageUrl để cập nhật thuộc tính url trong bảng sản phẩm
                                }  catch (JSONException | IOException e) {
                                    throw new RuntimeException(e);
                                }

                                System.out.println("upload hình thành công");
                            } else {
                                int statusCode = response.code();
                                ResponseBody errorBody = response.errorBody();
                                String errorMessage;
                                try {
                                    errorMessage = Objects.requireNonNull(response.errorBody()).string();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(statusCode);
                                System.out.println(errorMessage);
                                try {
                                    errorMessage = errorBody != null ? errorBody.string() : "";
                                    System.out.println(errorMessage);
                                } catch (IOException e) {
                                    Toast.makeText(AddProductPageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
