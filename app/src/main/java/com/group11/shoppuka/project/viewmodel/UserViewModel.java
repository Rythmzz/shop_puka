package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.model.account.User;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class UserViewModel extends ViewModel {
    private ApiService apiService;
    @Inject
    public UserViewModel(ApiService apiService){
        this.apiService = apiService;
    }
    public void updateAvatarUser(int id ,UserRequest userRequest){

        apiService.updateAvatar(id,userRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    System.out.println("Cập nhật avatar thành công");
                }
                else {
                    try {
                        String errormessage = response.errorBody().string();
                        System.out.println(errormessage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
}
