package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductRequest;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.view.repo.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class ProductViewModel extends ViewModel {

    private Repository repository;
    @Inject
    public ProductViewModel(Repository repository){
        this.repository = repository;
    }
    private MutableLiveData<ProductResponse> productResponseLiveData= new MutableLiveData<>();

    public LiveData<ProductResponse> getProductResponseLiveData(){
        return productResponseLiveData;
    }

    private MutableLiveData<Boolean> productIsAvalible = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> getProductIsAvalible() {
        return productIsAvalible;
    }

    public void fetchData(){
        repository.fetchDataProduct(new BaseCallback<ProductResponse>() {
            @Override
            public void onSuccess(BaseResponse<ProductResponse> responseSuccess) {
                BaseResponse.Success<ProductResponse> currentResponse = (BaseResponse.Success<ProductResponse>) responseSuccess;
                productResponseLiveData.setValue(currentResponse.getData());
                productIsAvalible.setValue(true);
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
    public void createNewProduct(ProductRequest productRequest){
        repository.createNewProduct(productRequest, new BaseCallback<String>() {
            @Override
            public void onSuccess(BaseResponse<String> responseSuccess) {
                System.out.println(responseSuccess.toString());
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
    public void updateData(int id, ProductRequest productRequest){

        repository.updateDataProduct(id, productRequest, new BaseCallback<String>() {
            @Override
            public void onSuccess(BaseResponse<String> responseSuccess) {
                System.out.println(responseSuccess.toString());
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
    public void deleteData(int id){
        repository.deleteDataProduct(id, new BaseCallback<String>() {
            @Override
            public void onSuccess(BaseResponse<String> responseSuccess) {
                System.out.println(responseSuccess.toString());
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
