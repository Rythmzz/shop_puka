package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class CategoryViewModel extends ViewModel {
    private ApiService apiService;

    @Inject
    public CategoryViewModel(ApiService apiService){
        this.apiService = apiService;
    }
    private MutableLiveData<CategoryResponse> categoryResponseLiveData = new MutableLiveData<>();

    public LiveData<CategoryResponse> getCategoryResponseLiveData(){
        return categoryResponseLiveData;
    }

    public void fetchDataCategory(){

        apiService.getListCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                CategoryResponse categoryResponse = response.body();
                categoryResponseLiveData.setValue(categoryResponse);
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
