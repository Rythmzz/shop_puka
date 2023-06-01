package com.group11.shoppuka.project.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.R;
import com.group11.shoppuka.project.view.fragment.CheckoutFragment;

public class CheckoutActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, checkoutFragment).commit();
    }
}
