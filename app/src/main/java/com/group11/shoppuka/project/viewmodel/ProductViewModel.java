package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductRequest;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {
    private MutableLiveData<ProductResponse> productResponseLiveData= new MutableLiveData<>();

    public LiveData<ProductResponse> getProductResponseLiveData(){
        return productResponseLiveData;
    }

    public void fetchData(){
        RetrofitService retrofitService = new RetrofitService();

        ApiService apiService = retrofitService.retrofit.create(ApiService.class);

        apiService.getListProduct().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ProductResponse productResponse = response.body();
                System.out.println(response.body());
                productResponseLiveData.setValue(productResponse);
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void createNewProduct(ProductRequest productRequest){
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.retrofit.create(ApiService.class);

        apiService.createProduct(productRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    System.out.println("Tạo product thành công");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
            }
        });
    }

    public void updateData(int id, ProductRequest productRequest){
        RetrofitService retrofitService = new RetrofitService();

        ApiService apiService = retrofitService.retrofit.create(ApiService.class);

        apiService.updateProduct(id,productRequest).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()){
                    System.out.println("Update sản phẩm thành công");
                }

            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteData(int id){
        RetrofitService retrofitService = new RetrofitService();

        ApiService apiService = retrofitService.retrofit.create(ApiService.class);

        apiService.deleteProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()){
                    System.out.println("Xóa hết sản phẩm thành công");
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
    }



}
