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

    private final Repository repository;

    @Inject
    public CartViewModel(Repository repository){
        this.repository = repository;
    }
    private final MutableLiveData<CartResponse> cartResponseMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<CartResponse> getCartResponseMutableLiveData() {
        return cartResponseMutableLiveData;
    }


    public void addCart(CartRequest cartRequest){
        repository.addCart(cartRequest, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                System.out.println(responseSuccess.toString());
                fetchListCart();

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


//    public void updateListCart(int id ,CartRequest cartRequest,BaseCallback callback){
//        repository.updateListCart(id, cartRequest, new BaseCallback() {
//            @Override
//            public void onSuccess(BaseResponse responseSuccess) {
//                System.out.println(responseSuccess.toString());
//            }
//
//            @Override
//            public void onError(BaseResponse responseError) {
//                System.out.println(responseError.toString());
//            }
//
//            @Override
//            public void onLoading() {
//                System.out.println("Loading....");
//            }
//        });
//    }

    public void deleteIdCart(int id){
        repository.deleteIdCart(id, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                System.out.println(responseSuccess.toString());
                fetchListCart();
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

    public void fetchListCart(){
        repository.fetchListCart(new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                BaseResponse.Success<CartResponse> cartResponseSuccess = (BaseResponse.Success<CartResponse>) responseSuccess;
                cartResponseMutableLiveData.setValue(cartResponseSuccess.getData());
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
