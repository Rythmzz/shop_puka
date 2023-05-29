package com.group11.shoppuka.project.model.cart;

import java.util.ArrayList;
import java.util.Arrays;

public class AttributesCart {
    private int idProduct;
    private int count;
    private int totalPrice;
    private String phoneNumber;

    private ArrayList<Integer> idResource = new ArrayList<>();

    public void addIdResource(int x){
        idResource.add(x);
    }

    public ArrayList<Integer> getIdResource() {
        return idResource;
    }

    public void setIdResource(ArrayList<Integer> idResource) {
        this.idResource = idResource;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
