package com.group11.shoppuka.project.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.service.ApiService;
import com.group11.shoppuka.project.service.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends ViewModel {

    private MutableLiveData<CartResponse> cartResponseMutableLiveData = new MutableLiveData<>();


    public MutableLiveData<CartResponse> getCartResponseMutableLiveData() {
        return cartResponseMutableLiveData;
    }

    public void setCartResponseMutableLiveData(CartResponse cartResponseMutableLiveData){
        this.cartResponseMutableLiveData.setValue(cartResponseMutableLiveData);
    }


    public void addCart(CartRequest cartRequest, Context context){

        RetrofitService retrofitService = new RetrofitService();
        ApiService myApi = retrofitService.retrofit.create(ApiService.class);

        myApi.addProductCart(cartRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context,"Thêm sản phẩm vào thành công !",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context,response.errorBody().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void updateListCart(int id ,CartRequest cartRequest){
        RetrofitService retrofitService = new RetrofitService();
        ApiService myApi = retrofitService.retrofit.create(ApiService.class);

        myApi.updateCart(id,cartRequest).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                System.out.println("cập nhật thành công");
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteIdCart(int id){
        RetrofitService retrofitService = new RetrofitService();
        ApiService myApi = retrofitService.retrofit.create(ApiService.class);

        myApi.deleteCart(id).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                System.out.println("Xóa thành công");
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public void fetchListCart(Context context){
        RetrofitService retrofitService = new RetrofitService();
        ApiService apiService = retrofitService.retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = context.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        String numberPhone = sharedPreferences.getString("phone_info",null);

        apiService.getListCart().enqueue(new Callback<CartResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                CartResponse cartResponse = response.body();

                Map<String, Cart> map = new HashMap<>();
                for (Cart item : cartResponse.getData()) {
                    String key = item.getAttributes().getIdProduct() + "-" + item.getAttributes().getPhoneNumber();
                    if (map.containsKey(key)) {
                        Cart existingItem = map.get(key);
                        existingItem.getAttributes().addIdResource(item.getId());
                        existingItem.getAttributes().setCount(existingItem.getAttributes().getCount() + item.getAttributes().getCount());
                    } else {
                        map.put(key, item);
                    }
                }
                List<Cart> result = new ArrayList<>(map.values());
                List<Cart> resultFull = result.stream().filter(item -> item.getAttributes().getPhoneNumber().equals(numberPhone)).collect(Collectors.toList());
                for (Cart cart : resultFull) {
                    System.out.println(cart.getAttributes().getPhoneNumber());
                    System.out.println(cart.getAttributes().getIdProduct());
                    System.out.println(cart.getAttributes().getCount());
                    System.out.println(cart.getAttributes().getIdResource());
                }
                CartResponse cartResponseFull = new CartResponse();
                cartResponseFull.setData(resultFull);
                cartResponseMutableLiveData.setValue(cartResponseFull);
                // Sử dụng kết quả lọc ở đây
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
