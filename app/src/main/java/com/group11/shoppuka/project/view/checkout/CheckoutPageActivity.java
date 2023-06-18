package com.group11.shoppuka.project.view.checkout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.R;
import com.group11.shoppuka.project.view.checkout.fragment.CheckoutFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CheckoutPageActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setUI();
    }

    private void setUI() {
        CheckoutFragment checkoutFragment = new CheckoutFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.checkout_fragment_container, checkoutFragment).commit();
    }
}
