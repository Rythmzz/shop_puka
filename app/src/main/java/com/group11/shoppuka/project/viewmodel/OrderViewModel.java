package com.group11.shoppuka.project.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.order.Order;
import com.group11.shoppuka.project.model.order.OrderRequest;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductRequest;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@HiltViewModel
public class OrderViewModel extends ViewModel {
    private ApiService apiService;
    @Inject
    public OrderViewModel(ApiService apiService){
        this.apiService = apiService;
    }
    private MutableLiveData<OrderResponse> orderResponseMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<OrderResponse> getOrderResponseMutableLiveData() {
        return orderResponseMutableLiveData;
    }

    public void createOrder(OrderRequest orderRequest){


        apiService.createOrder(orderRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    System.out.println("Tạo một order thành công");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void fetchData(String numberPhone){

        apiService.getListOrderWithNumberPhone().enqueue(new Callback<OrderResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                OrderResponse orderResponse = response.body();
                List<Order> resultFull =orderResponse.getData().stream().filter(item -> item.getAttributes().getPhoneNumber().equals(numberPhone)).collect(Collectors.toList());
                OrderResponse orderResponseFull = new OrderResponse();
                orderResponseFull.setData(resultFull);
                orderResponseMutableLiveData.setValue(orderResponseFull);
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void updateData(int id, OrderRequest orderRequest){
        apiService.updateOrder(id,orderRequest).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()){
                    System.out.println("Update Order Thành Công");
                }

            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void fetchListData(int startStatus, int endStatus){

        apiService.getListOrderWithNumberPhone().enqueue(new Callback<OrderResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                OrderResponse orderResponse = response.body();
                List<Order> resultFull =orderResponse.getData().stream().filter(item -> (item.getAttributes().getStatus() >= startStatus && item.getAttributes().getStatus() <= endStatus)).collect(Collectors.toList());
                for (Order order : resultFull) {
                    System.out.println(order.getAttributes().getPhoneNumber());
                    System.out.println(order.getAttributes().getIdProduct());
                    System.out.println(order.getAttributes().getQuantity());
                }
                OrderResponse orderResponseFull = new OrderResponse();
                orderResponseFull.setData(resultFull);
                orderResponseMutableLiveData.setValue(orderResponseFull);
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
