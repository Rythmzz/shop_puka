package com.group11.shoppuka.project.view.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.group11.shoppuka.project.base.BaseCallback;
import com.group11.shoppuka.project.base.BaseResponse;
import com.group11.shoppuka.project.model.account.User;
import com.group11.shoppuka.project.model.account.UserRequest;
import com.group11.shoppuka.project.model.account.UserResponse;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.model.order.Order;
import com.group11.shoppuka.project.model.order.OrderRequest;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductRequest;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.service.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton public class Repository {
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    @Inject
    public Repository(ApiService apiService, SharedPreferences sharedPreferences){
        this.apiService = apiService;
        this.sharedPreferences = sharedPreferences;
    }

    // Feature Cart
    public void addCart(CartRequest cartRequest, BaseCallback<String> callback){
        callback.onLoading();
        apiService.addProductCart(cartRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success("Thêm sản phẩm thành công !"));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });

    }

    public void deleteIdCart(int id, BaseCallback<String> callback){
        callback.onLoading();
        apiService.deleteCart(id).enqueue(new Callback<Cart>() {

            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) callback.onSuccess(new BaseResponse.Success<>("Xóa vật phẩm ra giỏ hàng thành công"));
                else callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    public void fetchListCart(BaseCallback<CartResponse> callback){
        callback.onLoading();
        String numberPhone = sharedPreferences.getString(MyApplication.KEY_ACCOUNT_PHONE,null);
        apiService.getListCart(numberPhone,0,-1).enqueue(new Callback<CartResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()){
                    CartResponse cartResponse = response.body();
                    Map<String, Cart> map = new HashMap<>();
                    for (Cart cart: response.body().getData()){
                        System.out.println("IDPRODUCT=" +cart.getAttributes().getIdProduct());
                        System.out.println("COUNT=" +cart.getAttributes().getCount());
                        System.out.println("PHONE" + cart.getAttributes().getPhoneNumber());
                    }
                    for (Cart item : cartResponse.getData()) {
                        String key = item.getAttributes().getIdProduct() + "-" + item.getAttributes().getPhoneNumber();
                        if (map.containsKey(key)) {
                            Cart existingItem = map.get(key);
                            System.out.println("KEY ="+ key);
                            existingItem.getAttributes().addIdResource(item.getId());
                            existingItem.getAttributes().setCount(existingItem.getAttributes().getCount() + item.getAttributes().getCount());
                        } else {
                            map.put(key, item);
                        }
                    }
                    List<Cart> result = new ArrayList<>(map.values());
                    List<Cart> resultFull = result.stream().filter(item -> item.getAttributes().getPhoneNumber().equals(numberPhone)).collect(Collectors.toList());
                    CartResponse cartResponseFull = new CartResponse();
                    cartResponseFull.setData(resultFull);
                    callback.onSuccess(new BaseResponse.Success(cartResponseFull));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    public void updateListCart(int id ,CartRequest cartRequest, BaseCallback<String> callback){
        callback.onLoading();
        apiService.updateCart(id,cartRequest).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success<>("Cập nhật thành công"));
                }
                else{
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    // feature Category

    public void fetchDataCategory(BaseCallback<CategoryResponse> callback){
        callback.onLoading();
        apiService.getListCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                if (response.isSuccessful()){
                    CategoryResponse categoryResponse = response.body();
                    callback.onSuccess(new BaseResponse.Success<>(categoryResponse));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    // Feature User Login, Register

    public void fetchUser(BaseCallback<UserResponse> callback){
        callback.onLoading();
        apiService.getListUser().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful()){
                    UserResponse userResponse = response.body();
                    callback.onSuccess(new BaseResponse.Success<UserResponse>(userResponse));
                }
                else{
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }
    public void signUpUser(UserRequest userRequest, Context context, BaseCallback<String> callback){

        Call<ResponseBody> call = apiService.createUser(userRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.onLoading();
                if (response.isSuccessful()) {
                    callback.onSuccess(new BaseResponse.Success<>("Đăng ký thành công!"));
                } else {
                    int statusCode = response.code();
                    if (statusCode == 400){
                        callback.onError(new BaseResponse.Error(new Exception("Số tài khoản này đã được sử dụng !!")));
                    }
                    else {
                        ResponseBody errorBody = response.errorBody();
                        String errorMessage = "";
                        try {
                            errorMessage = errorBody != null ? errorBody.string() : "";
                        } catch (IOException e) {
                            callback.onError(new BaseResponse.Error(new Exception(e.getMessage())));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });

    }

    // Feature Order

    public void createOrder(OrderRequest orderRequest, BaseCallback<String> callback){
        callback.onLoading();
        apiService.createOrder(orderRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success<>("Tạo một order thành công"));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    public void fetchDataOrder(String numberPhone, BaseCallback<OrderResponse> callback){
        callback.onLoading();
        apiService.getListOrderWithNumberPhone().enqueue(new Callback<OrderResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    OrderResponse orderResponse = response.body();
                    List<Order> resultFull =orderResponse.getData().stream().filter(item -> item.getAttributes().getPhoneNumber().equals(numberPhone)).collect(Collectors.toList());
                    OrderResponse orderResponseFull = new OrderResponse();
                    orderResponseFull.setData(resultFull);
                    callback.onSuccess(new BaseResponse.Success<>(orderResponseFull));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }
    public void updateDataOrder(int id, OrderRequest orderRequest, BaseCallback<String> callback){
        callback.onLoading();
        apiService.updateOrder(id,orderRequest).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success<>("Tạo một order thành công"));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }
    public void fetchListDataOrder(int startStatus, int endStatus, BaseCallback<OrderResponse> callback){
        callback.onLoading();
        apiService.getListOrderWithNumberPhone().enqueue(new Callback<OrderResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()){
                    OrderResponse orderResponse = response.body();
                    List<Order> resultFull =orderResponse.getData().stream().filter(item -> (item.getAttributes().getStatus() >= startStatus && item.getAttributes().getStatus() <= endStatus)).collect(Collectors.toList());
                    OrderResponse orderResponseFull = new OrderResponse();
                    orderResponseFull.setData(resultFull);
                    callback.onSuccess(new BaseResponse.Success<>(orderResponseFull));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }


            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    // feature product

    public void fetchDataProduct(BaseCallback<ProductResponse> callback){
        callback.onLoading();
        apiService.getListProduct().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()){
                    ProductResponse productResponse = response.body();
                    callback.onSuccess(new BaseResponse.Success<>(productResponse));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }

            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    public void createNewProduct(ProductRequest productRequest, BaseCallback<String> callback){
        callback.onLoading();
        apiService.createProduct(productRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success<>("Tạo product thành công"));
                }
                else{
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    public void updateDataProduct(int id, ProductRequest productRequest, BaseCallback<String> callback){

        apiService.updateProduct(id,productRequest).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success<>("Cập nhật sản phaark"));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }

            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
    });
    }


    public void deleteDataProduct(int id, BaseCallback<String> callback){

        apiService.deleteProduct(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success<>("Xóa sản phẩm thành công"));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }

            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

    // feature User

    public void updateAvatarUser(int id ,UserRequest userRequest, BaseCallback<String> callback){

        apiService.updateAvatar(id,userRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(new BaseResponse.Success<>("Cập nhật avatar thành công"));
                }
                else {
                    callback.onError(new BaseResponse.Error(new Exception(response.errorBody().toString())));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(new BaseResponse.Error(new Exception(t.getMessage())));
            }
        });
    }

}
