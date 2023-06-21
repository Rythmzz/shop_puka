package com.group11.shoppuka.project.base;

public interface BaseCallback<T> {
    void onSuccess(BaseResponse<T> responseSuccess);
    void onError(BaseResponse<Exception> responseError);
    void onLoading();
}
