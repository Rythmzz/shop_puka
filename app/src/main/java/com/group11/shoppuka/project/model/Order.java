package com.group11.shoppuka.project.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Order implements Parcelable {
    private String productName;
    private int quantity;
    private double totalPrice;
    private String imageUrl;
    private Date orderAt;

    public Order(String productName, int quantity, double totalPrice, String imageUrl, Date orderAt) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
        this.orderAt = orderAt;
    }

    public Order(String productName) {
        this.productName = productName;
    }
    public Order() {

    }
    protected Order(Parcel in) {
        productName = in.readString();
        quantity = in.readInt();
        totalPrice = in.readDouble();
        imageUrl = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeInt(quantity);
        dest.writeDouble(totalPrice);
        dest.writeString(imageUrl);
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Date orderAt) {
        this.orderAt = orderAt;
    }

}
