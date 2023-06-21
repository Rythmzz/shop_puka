package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.view.repo.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CartViewModel extends ViewModel {

    private Repository repository;

    @Inject
    public CartViewModel(Repository repository){
        this.repository = repository;
    }
    private MutableLiveData<CartResponse> cartResponseMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<CartResponse> getCartResponseMutableLiveData() {
        return cartResponseMutableLiveData;
    }


    public void addCart(CartRequest cartRequest){
        repository.addCart(cartRequest, new BaseCallback<String>() {
            @Override
            public void onSuccess(BaseResponse<String> responseSuccess) {
                System.out.println(responseSuccess.toString());
                fetchListCart();

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


    public void updateListCart(int id ,CartRequest cartRequest,BaseCallback<String> callback){
        repository.updateListCart(id, cartRequest, new BaseCallback<String>() {
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

    public void deleteIdCart(int id){
        repository.deleteIdCart(id, new BaseCallback<String>() {
            @Override
            public void onSuccess(BaseResponse<String> responseSuccess) {
                System.out.println(responseSuccess.toString());
                fetchListCart();
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

    public void fetchListCart(){
        repository.fetchListCart(new BaseCallback<CartResponse>() {
            @Override
            public void onSuccess(BaseResponse<CartResponse> responseSuccess) {
                BaseResponse.Success<CartResponse> cartResponseSuccess = (BaseResponse.Success<CartResponse>) responseSuccess;
                cartResponseMutableLiveData.setValue(cartResponseSuccess.getData());
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
