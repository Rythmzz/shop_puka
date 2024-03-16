package com.group11.shoppuka.project.model.order;

import java.util.List;

public class OrderResponse {

    private List<Order> data;

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

    public void removeDataSpecific(int position){
        this.data.remove(position);
    }
}
