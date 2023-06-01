package com.group11.shoppuka.project.view;

import static kotlin.io.ByteStreamsKt.readBytes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.databinding.ActivityModifyProductBinding;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.category.Category;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductData;
import com.group11.shoppuka.project.model.product.ProductRequest;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.viewmodel.CartViewModel;
import com.group11.shoppuka.project.viewmodel.CategoryViewModel;
import com.group11.shoppuka.project.viewmodel.ProductViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyProductActivity extends AppCompatActivity {

    ActivityModifyProductBinding binding;

    private CategoryViewModel cateViewModel;

    private ProductData productData = new ProductData();

    @Override
    protected void onResume() {
        super.onResume();
    }

    String imageUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");
        getSupportActionBar().setTitle(product.getAttributes().getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F87217"));
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainColor)));
        String url = MyApplication.localHost + product.getAttributes().getImageURL();
        Glide.with(this).load(url).into(binding.ivImageURL);
        binding.edtProductName.setText(product.getAttributes().getName());
        binding.edtProductPrice.setText(String.valueOf(product.getAttributes().getPrice()));
        binding.edtProductSalePrice.setText(String.valueOf(product.getAttributes().getSalePrice()));
        productData.setIdCategory(product.getAttributes().getIdCategory());
        binding.edtProductDescription.setText(product.getAttributes().getDescription());
        cateViewModel = new ViewModelProvider(ModifyProductActivity.this).get(CategoryViewModel.class);
        cateViewModel.fetchDataCategory();
        CategoryResponse currentCategoryResponse = new CategoryResponse();
        ArrayList<String> data = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,data);
        cateViewModel.getCategoryResponseLiveData().observe(ModifyProductActivity.this, new Observer<CategoryResponse>() {
            @Override
            public void onChanged(CategoryResponse categoryResponse) {
                currentCategoryResponse.setData(categoryResponse.getData());
                for (Category category : currentCategoryResponse.getData()){
                    data.add(category.getAttributes().getName());
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spnCategory.setAdapter(adapter);
                binding.spnCategory.setSelection(product.getAttributes().getIdCategory()-1);

            }
        });



        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = data.get(i);
                productData.setIdCategory(i+1);
                System.out.println("ID CATEGORY : "+ i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtProductName.getText().toString().isEmpty()){
                    binding.edtProductName.setError("Tên sản phẩm không được để trống");
                }
                else if (binding.edtProductPrice.getText().toString().isEmpty()){
                    binding.edtProductPrice.setError("Giá sản phẩm không được để trống");
                }
                else if (binding.edtProductSalePrice.getText().toString().isEmpty()){
                    binding.edtProductSalePrice.setError("Giá khuyến mãi không được để trống");
                }
                else if (Integer.valueOf(binding.edtProductPrice.getText().toString()) <= Integer.valueOf(binding.edtProductSalePrice.getText().toString())){
                    binding.edtProductSalePrice.setError("Giá khuyến mãi không được lớn hơn giá gốc");
                }
                else if (binding.edtProductDescription.getText().toString().isEmpty()){
                    binding.edtProductDescription.setError("Mô tả sản phẩm không được để trống");
                }
                else {
                    if (productData.getImageURL() == null){
                       productData.setImageURL(product.getAttributes().getImageURL());
                    }
                    productData.setName(binding.edtProductName.getText().toString());
                    productData.setPrice(Integer.valueOf(binding.edtProductPrice.getText().toString()));
                    productData.setSalePrice(Integer.valueOf(binding.edtProductSalePrice.getText().toString()));
                    productData.setDescription(binding.edtProductDescription.getText().toString());
                    productData.setCountSearch(0);
                    ProductRequest productRequest = new ProductRequest();
                    productRequest.setData(productData);
                    ProductViewModel productViewModel = new ViewModelProvider(ModifyProductActivity.this).get(ProductViewModel.class);
                    productViewModel.updateData(product.getId(),productRequest);
                    Toast.makeText(getApplicationContext(),"Product được chỉnh sửa thành công !",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        binding.ivImageURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),MyApplication.PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyApplication.PICK_IMAGE){
            if (resultCode == RESULT_OK){
                Uri imageUri = data.getData();
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
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {
                                try {
                                    String responseBody = response.body().string();
                                    JSONArray jsonArray = new JSONArray(responseBody);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    imageUrl = jsonObject.getString("url");
                                    productData.setImageURL(imageUrl);
                                    System.out.println(imageUrl);
                                    // Sử dụng imageUrl để cập nhật thuộc tính url trong bảng sản phẩm
                                }  catch (JSONException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                System.out.println("upload hình thành công");
                            } else {
                                int statusCode = response.code();
                                ResponseBody errorBody = response.errorBody();
                                String errorMessage = null;
                                try {
                                    errorMessage = response.errorBody().string();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(statusCode);
                                System.out.println(errorMessage);
                                try {
                                    errorMessage = errorBody != null ? errorBody.string() : "";
                                } catch (IOException e) {
                                    Toast.makeText(ModifyProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }



        }
    }
}