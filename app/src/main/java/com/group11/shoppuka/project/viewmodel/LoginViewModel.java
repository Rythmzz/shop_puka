package com.group11.shoppuka.project.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.model.account.UserResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.view.repo.Repository;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class LoginViewModel extends ViewModel {
    private Repository repository;
    @Inject
    public LoginViewModel(Repository repository){
        this.repository = repository;
    }
    private MutableLiveData<Boolean> signUpSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> progressLoading = new MutableLiveData<>();

    private MutableLiveData<UserResponse> userResponseMutableLiveData = new MutableLiveData<>();

    public LiveData<UserResponse> getListUser(){
        return userResponseMutableLiveData;
    }
    public LiveData<Boolean> getSignUpSuccess(){
        return  signUpSuccessLiveData;
    }
    public LiveData<Boolean> getProgressLoading(){
        return progressLoading;
    }

    public void fetchUser(){
        repository.fetchUser(new BaseCallback<UserResponse>() {
            @Override
            public void onSuccess(BaseResponse<UserResponse> responseSuccess) {
                BaseResponse.Success<UserResponse> currentResponse = (BaseResponse.Success<UserResponse>) responseSuccess;
                userResponseMutableLiveData.setValue(currentResponse.getData());
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
    public void signUpUser(UserRequest userRequest, Context context){
        repository.signUpUser(userRequest, context, new BaseCallback<String>() {
            @Override
            public void onSuccess(BaseResponse<String> responseSuccess) {
                System.out.println(responseSuccess.toString());
                signUpSuccessLiveData.setValue(true);
                progressLoading.setValue(true);
            }

            @Override
            public void onError(BaseResponse<Exception> responseError) {
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
