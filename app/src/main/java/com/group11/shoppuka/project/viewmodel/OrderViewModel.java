package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.order.OrderRequest;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.view.repo.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class OrderViewModel extends ViewModel {
    private final Repository repository;
    @Inject
    public OrderViewModel(Repository repository){
        this.repository = repository;
    }
    private final MutableLiveData<OrderResponse> orderResponseMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<OrderResponse> getOrderResponseMutableLiveData() {
        return orderResponseMutableLiveData;
    }

    public void createOrder(OrderRequest orderRequest){
        repository.createOrder(orderRequest, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                System.out.println(responseSuccess.toString());
            }
            @Override
            public void onError(BaseResponse responseError) {
                System.out.println(responseError.toString());
            }
            @Override
            public void onLoading() {
                System.out.println("Loading....");
            }
        });
    }

    public void fetchData(String numberPhone){

        repository.fetchDataOrder(numberPhone, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                BaseResponse.Success<OrderResponse> currentResponse = (BaseResponse.Success<OrderResponse>) responseSuccess;
                orderResponseMutableLiveData.setValue(currentResponse.getData());
            }

            @Override
            public void onError(BaseResponse responseError) {
                System.out.println(responseError.toString());
            }

            @Override
            public void onLoading() {
                System.out.println("Loading....");
            }
        });
    }
    public void updateData(int id, OrderRequest orderRequest){
        repository.updateDataOrder(id, orderRequest, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                System.out.println(responseSuccess.toString());
            }
            @Override
            public void onError(BaseResponse responseError) {
                System.out.println(responseError.toString());
            }
            @Override
            public void onLoading() {
                System.out.println("Loading....");
            }
        });
    }
    public void fetchListData(int startStatus, int endStatus){

        repository.fetchListDataOrder(startStatus, endStatus, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                BaseResponse.Success<OrderResponse> currentResponse = (BaseResponse.Success<OrderResponse>) responseSuccess;
                orderResponseMutableLiveData.setValue(currentResponse.getData());
            }

            @Override
            public void onError(BaseResponse responseError) {
                System.out.println(responseError.toString());
            }

            @Override
            public void onLoading() {
                System.out.println("Loading....");
            }
        });
    }

}
