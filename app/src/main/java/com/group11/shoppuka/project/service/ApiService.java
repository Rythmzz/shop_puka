package com.group11.shoppuka.project.service;

import com.group11.shoppuka.project.model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/products/{id}")
    Call<Product> getProduct(@Path("id") int id);
}
