package com.group11.shoppuka.project.service;

import android.net.Uri;

import com.group11.shoppuka.project.model.account.User;
import com.group11.shoppuka.project.model.account.UserData;
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

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface    ApiService {
    @GET("api/products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @GET("api/categories/")
    Call<CategoryResponse> getListCategory();

    @GET("api/products/")
    Call<ProductResponse> getListProduct();

    @POST("/api/accounts")
    Call<ResponseBody> createUser(@Body UserRequest userRequest);

    @GET("api/accounts/")
    Call<UserResponse> getListUser();

    @POST("/api/carts")
    Call<ResponseBody> addProductCart(@Body CartRequest cartRequest);

    @GET("/api/carts")
    Call<CartResponse> getListCart(@Query("filters[phoneNumber][$eq]") String phoneNumber,@Query("pagination[start]") int start, @Query("pagination[limit]") int limit);

    @PUT("/api/carts/{id}")
    Call<Cart> updateCart(@Path("id") int id, @Body CartRequest cartRequest);

    @DELETE("/api/carts/{id}")
    Call<Cart> deleteCart(@Path("id") int id);

    @Multipart
    @POST("/api/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);

    @POST("/api/products")
    Call<ResponseBody> createProduct(@Body ProductRequest productRequest);

    @PUT("/api/products/{id}")
    Call<Product> updateProduct(@Path("id") int id, @Body ProductRequest productRequest);

    @DELETE("/api/products/{id}")
    Call<Product> deleteProduct(@Path("id") int id);

    @POST("/api/orders")
    Call<ResponseBody> createOrder(@Body OrderRequest orderRequest);

    @GET("/api/orders")
    Call<OrderResponse> getListOrderWithNumberPhone();

    @PUT("/api/orders/{id}")
    Call<Order> updateOrder(@Path(("id")) int id, @Body OrderRequest orderRequest);

    @PUT("/api/accounts/{id}")
    Call<User> updateAvatar(@Path(("id")) int id, @Body UserRequest userRequest);

}
