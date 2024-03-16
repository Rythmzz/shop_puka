package com.group11.shoppuka.project.model.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ProductResponse implements Serializable {
    private List<Product> data;

    public ProductResponse(){
    }
    public ProductResponse(List<Product> data){
        this.data = data;
    }

    public List<Product> getData() {
        return data;
    }

    public void addProduct(Product product){
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(product);

    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
