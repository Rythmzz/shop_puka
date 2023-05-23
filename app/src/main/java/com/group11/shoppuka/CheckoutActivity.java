package com.group11.shoppuka;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.project.CheckoutFragment;
import com.group11.shoppuka.project.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity  {
    /*public void setListOrder(List<Order> listOrder) {
        this.ListOrder = new ArrayList();
        this.ListOrder = listOrder;
        Log.d("TEST@",ListOrder.get(0).getProductName());
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        List<Order> orderList = getIntent().getParcelableArrayListExtra("order_list");
        Log.d("TEST3", orderList.get(0).getProductName());
        //findViewById(R.id.checkout_fragment_container);
        CheckoutFragment checkoutFragment = new CheckoutFragment(orderList);
        getSupportFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, checkoutFragment).commit();
    }
}
