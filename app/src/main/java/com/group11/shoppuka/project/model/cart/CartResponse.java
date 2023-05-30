package com.group11.shoppuka.project.model.cart;

import java.io.Serializable;
import java.util.List;

public class CartResponse implements Serializable {
    List<Cart> data;

    public List<Cart> getData() {
        return data;
    }

    public void setData(List<Cart> data) {
        this.data = data;
    }
}
