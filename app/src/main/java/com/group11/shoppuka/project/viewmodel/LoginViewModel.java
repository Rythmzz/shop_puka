package com.group11.shoppuka.project.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.account.ClientResponse;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.model.account.UserResponse;
import com.group11.shoppuka.project.view.repo.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final Repository repository;
    @Inject
    public LoginViewModel(Repository repository){
        this.repository = repository;
    }
    private final MutableLiveData<Boolean> success = new MutableLiveData<>();
    private final MutableLiveData<Boolean> progressLoading = new MutableLiveData<>();

    private final MutableLiveData<UserResponse> userResponseMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<ClientResponse> currentLoginClient = new MutableLiveData<>();

    public LiveData<UserResponse> getListUser(){
        return userResponseMutableLiveData;
    }
    public LiveData<Boolean> getSignUpSuccess(){
        return  success;
    }
    public LiveData<Boolean> getProgressLoading(){
        return progressLoading;
    }

    public void login(String username, String password){
        repository.login(username, password, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                BaseResponse.Success<ClientResponse> currentClientResponse = (BaseResponse.Success<ClientResponse>) responseSuccess;
                currentLoginClient.setValue(currentClientResponse.getData());
                success.setValue(true);
            }

            @Override
            public void onError(BaseResponse responseError) {
                System.out.println(responseError.toString());
                success.setValue(false);
            }

            @Override
            public void onLoading() {

            }
        });
    }
    public void fetchUser(){
        repository.fetchUser(new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                BaseResponse.Success<UserResponse> currentResponse = (BaseResponse.Success<UserResponse>) responseSuccess;
                userResponseMutableLiveData.setValue(currentResponse.getData());

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
    public void signUpUser(UserRequest userRequest, Context context){
        repository.signUpUser(userRequest, new BaseCallback() {
            @Override
            public void onSuccess(BaseResponse responseSuccess) {
                System.out.println(responseSuccess.toString());
                success.setValue(true);
                progressLoading.setValue(true);
            }

            @Override
            public void onError(BaseResponse responseError) {
                progressLoading.setValue(true);
                System.out.println(responseError.toString());
            }

            @Override
            public void onLoading() {
                progressLoading.setValue(false);
                System.out.println("Loading....");
            }
        });

    }
}
