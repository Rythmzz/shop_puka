package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.account.User;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;
import com.group11.shoppuka.project.view.repo.Repository;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class UserViewModel extends ViewModel {
    private Repository repository;
    @Inject
    public UserViewModel(Repository repository){
        this.repository = repository;
    }
    public void updateAvatarUser(int id ,UserRequest userRequest){
        repository.updateAvatarUser(id, userRequest, new BaseCallback<String>() {
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
