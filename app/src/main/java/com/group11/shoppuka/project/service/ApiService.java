package com.group11.shoppuka.project.service;

import com.group11.shoppuka.project.model.category.Category;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.model.product.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface    ApiService {
    @GET("api/products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @GET("api/products/")
    Call<CategoryResponse> getListCategory();


}
