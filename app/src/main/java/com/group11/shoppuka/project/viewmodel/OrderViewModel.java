package com.group11.shoppuka.project.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.order.Order;
import com.group11.shoppuka.project.model.order.OrderRequest;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductRequest;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.view.repo.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class OrderViewModel extends ViewModel {
    private Repository repository;
    @Inject
    public OrderViewModel(Repository repository){
        this.repository = repository;
    }
    private MutableLiveData<OrderResponse> orderResponseMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<OrderResponse> getOrderResponseMutableLiveData() {
        return orderResponseMutableLiveData;
    }

    public void createOrder(OrderRequest orderRequest){
        repository.createOrder(orderRequest, new BaseCallback<String>() {
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

    public void fetchData(String numberPhone){

        repository.fetchDataOrder(numberPhone, new BaseCallback<OrderResponse>() {
            @Override
            public void onSuccess(BaseResponse<OrderResponse> responseSuccess) {
                BaseResponse.Success<OrderResponse> currentResponse = (BaseResponse.Success<OrderResponse>) responseSuccess;
                orderResponseMutableLiveData.setValue(currentResponse.getData());
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
    public void updateData(int id, OrderRequest orderRequest){
        repository.updateDataOrder(id, orderRequest, new BaseCallback<String>() {
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
    public void fetchListData(int startStatus, int endStatus){

        repository.fetchListDataOrder(startStatus, endStatus, new BaseCallback<OrderResponse>() {
            @Override
            public void onSuccess(BaseResponse<OrderResponse> responseSuccess) {
                BaseResponse.Success<OrderResponse> currentResponse = (BaseResponse.Success<OrderResponse>) responseSuccess;
                orderResponseMutableLiveData.setValue(currentResponse.getData());
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
