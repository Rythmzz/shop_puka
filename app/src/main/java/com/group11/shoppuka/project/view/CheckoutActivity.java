package com.group11.shoppuka.project.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.R;
import com.group11.shoppuka.project.view.fragment.CheckoutFragment;

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
//        List<Order> orderList = getIntent().getParcelableArrayListExtra("order_list");
//        Log.d("TEST3", orderList.get(0).getProductName());
        //findViewById(R.id.checkout_fragment_container);
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, checkoutFragment).commit();
    }
}
