package com.group11.shoppuka.project.viewmodel;

import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.view.repo.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final Repository repository;
    @Inject
    public UserViewModel(Repository repository){
        this.repository = repository;
    }

    public void updateAvatarUser(int id ,UserRequest userRequest){
        repository.updateAvatarUser(id, userRequest, new BaseCallback() {
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
}
