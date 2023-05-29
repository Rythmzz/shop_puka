package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {
    private MutableLiveData<CategoryResponse> categoryResponseLiveData = new MutableLiveData<>();

    public LiveData<CategoryResponse> getCategoryResponseLiveData(){
        return categoryResponseLiveData;
    }

    public void fetchDataCategory(){
        RetrofitService retrofitService = new RetrofitService();

        ApiService myApi = retrofitService.retrofit.create(ApiService.class);

        myApi.getListCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                CategoryResponse categoryResponse = response.body();
                categoryResponseLiveData.setValue(categoryResponse);
//                for (Category category : categories) {
//                    System.out.println("ID: " + category.getId());
//                    System.out.println("Name: " + category.getAttributes().getName());
//                    // In ra các thuộc tính khác của đối tượng Category
//                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
