package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.view.repo.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class CategoryViewModel extends ViewModel {

    private Repository repository;
    @Inject
    public CategoryViewModel(Repository repository){
        this.repository = repository;
    }
    private MutableLiveData<CategoryResponse> categoryResponseLiveData = new MutableLiveData<>();

    public LiveData<CategoryResponse> getCategoryResponseLiveData(){
        return categoryResponseLiveData;
    }

    public void fetchDataCategory(){
        repository.fetchDataCategory(new BaseCallback<CategoryResponse>() {
            @Override
            public void onSuccess(BaseResponse<CategoryResponse> responseSuccess) {
                BaseResponse.Success<CategoryResponse> currentCategory = (BaseResponse.Success<CategoryResponse>) responseSuccess;
                categoryResponseLiveData.setValue(currentCategory.getData());
            }

            @Override
            public void onError(BaseResponse<Exception> responseError) {
                System.out.println(responseError.toString());
            }

            @Override
            public void onLoading() {
                System.out.println("Loading....");
            }
        });
    }
}
