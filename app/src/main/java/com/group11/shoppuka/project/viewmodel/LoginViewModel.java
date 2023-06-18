package com.group11.shoppuka.project.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.model.account.UserResponse;
import com.group11.shoppuka.project.service.ApiService;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class LoginViewModel extends ViewModel {
    private ApiService apiService;
    @Inject
    public LoginViewModel(ApiService apiService){
        this.apiService = apiService;
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


        apiService.getListUser().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                userResponseMutableLiveData.setValue(userResponse);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
    public void signUpUser(UserRequest userRequest, Context context){

        Call<ResponseBody> call = apiService.createUser(userRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressLoading.setValue(false);


                if (response.isSuccessful()) {
                    signUpSuccessLiveData.setValue(true);
                    progressLoading.setValue(true);
                } else {
                    int statusCode = response.code();
                    progressLoading.setValue(true);
                    if (statusCode == 400){
                        Toast.makeText(context,"Số điện thoại đã được sử dụng !!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ResponseBody errorBody = response.errorBody();
                        String errorMessage = "";
                        try {
                            errorMessage = errorBody != null ? errorBody.string() : "";
                        } catch (IOException e) {
                            progressLoading.setValue(true);
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    // In ra mã trạng thái HTTP và thông báo lỗi từ Strapi
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }
}
