package com.group11.shoppuka.project.base;

public interface BaseCallback {
    void onSuccess(BaseResponse responseSuccess);
    void onError(BaseResponse responseError);
    void onLoading();
}
