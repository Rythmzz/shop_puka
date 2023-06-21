package com.group11.shoppuka.project.model.product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ProductResponse  {
    private List<Product> data;

    public ProductResponse(){
    }
    public ProductResponse(List<Product> data){
        this.data = data;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
