package com.group11.shoppuka;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.group11.shoppuka.project.bean.CartItemBean;
import com.group11.shoppuka.project.bean.GoodsBean;
import com.group11.shoppuka.project.bean.ICartItem;
import com.group11.shoppuka.project.bean.ShopBean;
import com.group11.shoppuka.project.listener.CartOnCheckChangeListener;
import com.group11.shoppuka.project.adapter.MainAdapter;
import com.group11.shoppuka.project.model.Attributes;
import com.group11.shoppuka.project.model.Order;
import com.group11.shoppuka.project.model.Product;
import com.group11.shoppuka.project.service.ApiService;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView mTvTitle;
    private TextView mTvEdit;
    private CheckBox mCheckBoxAll;
    private TextView mTvTotal;
    private Button mBtnSubmit;
    int count=0;int productIndex=1;int i=0;
    ArrayList<CartItemBean> cartItemBeans = new ArrayList<>();
    List data;
    public List<String> brandNameList = new ArrayList<>();
    public List<String> NameList = new ArrayList<>();
    MainAdapter mAdapter;
    // danh sách đơn hàng
    public List<Order> oderList = new ArrayList<>();
    private boolean isEditing;
    private int totalCount;
    private int totalCheckedCount;
    private double totalPrice;
    // lấy thời gian hệ thống
    Calendar calendar = Calendar.getInstance();
    Date currentTime = calendar.getTime();

    FirebaseFirestore db;
    //requestWindowFeature(Window.FEATURE_NO_TITLE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.cart_main);

        recyclerView = ((RecyclerView) findViewById(R.id.recycler));
        mTvTitle = ((TextView) findViewById(R.id.tv_title));
        mTvEdit = ((TextView) findViewById(R.id.tv_edit));
        mCheckBoxAll = ((CheckBox) findViewById(R.id.checkbox_all));
        mTvTotal = ((TextView) findViewById(R.id.tv_total_price));
        mBtnSubmit = ((Button) findViewById(R.id.btn_go_to_pay));

        mTvEdit.setOnClickListener(this);
        mCheckBoxAll.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);

        mTvTitle.setText(getString(R.string.cart, 0));
        mBtnSubmit.setText(getString(R.string.go_settle_X, 0));
        mTvTotal.setText(getString(R.string.rmb_X, 0.00));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this, getData());
        mAdapter.setCanCollapsing(true);
        mAdapter.setOnCheckChangeListener(new CartOnCheckChangeListener(recyclerView, mAdapter) {
            @Override
            public void onCalculateChanged(ICartItem cartItemBean) {
                calculate();
            }
        });
        recyclerView.setAdapter(mAdapter);

        registerForContextMenu(recyclerView);
    }


    private void calculate() {
        totalCheckedCount = 0;
        totalCount = 0;
        totalPrice = 0.00;
        int notChildTotalCount = 0;
        if (mAdapter.getData() != null) {
            for (ICartItem iCartItem : mAdapter.getData()) {
                if (iCartItem.getItemType() == ICartItem.TYPE_CHILD) {
                    totalCount++;
                    if (iCartItem.isChecked()) {
                        totalCheckedCount++;
                        totalPrice += ((GoodsBean) iCartItem).getGoods_price() * ((GoodsBean) iCartItem).getGoods_amount();
                    }
                } else {
                    notChildTotalCount++;
                }
            }
        }

        mTvTitle.setText(getString(R.string.cart, totalCount));
        mBtnSubmit.setText(getString(isEditing ? R.string.delete_X : R.string.go_settle_X, totalCheckedCount));
        //Quy đổi tiền tệ
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(0);
        nf.setCurrency(Currency.getInstance("VND"));
        String formattedPrice = nf.format(totalPrice);
        mTvTotal.setText(formattedPrice);
        if (mCheckBoxAll.isChecked() && (totalCheckedCount == 0 || (totalCheckedCount + notChildTotalCount) != mAdapter.getData().size())) {
            mCheckBoxAll.setChecked(false);
        }
        if (totalCheckedCount != 0 && (!mCheckBoxAll.isChecked()) && (totalCheckedCount + notChildTotalCount) == mAdapter.getData().size()) {
            mCheckBoxAll.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_edit:
                isEditing = !isEditing;
                mTvEdit.setText(getString(isEditing ? R.string.edit_done : R.string.edit));
                mBtnSubmit.setText(getString(isEditing ? R.string.delete_X : R.string.go_settle_X, totalCheckedCount));
                break;

            case R.id.btn_go_to_pay:
                submitEvent();
                break;
            case R.id.checkbox_all:
                mAdapter.checkedAll(((CheckBox) v).isChecked());
                break;
            default:
                break;
        }
    }

    private void submitEvent() {
        // chế độ chỉnh sửa đơn hàng
        if (isEditing) {
            if (totalCheckedCount == 0) {}
            else {
                mAdapter.removeChecked();
            }
        }
        // chế độ mua hàng
        else {
            if (totalCheckedCount == 0) {}
            // kiểm tra các đơn hàng được chọn và gán danh sách vào orderList
            else {
                if (mAdapter.getData() != null) {
                for (ICartItem iCartItem : mAdapter.getData()) {
                    if (iCartItem.getItemType() == ICartItem.TYPE_CHILD) {
                        if (iCartItem.isChecked()) {
                            Order order = new Order();
                            order.setImageUrl(((GoodsBean)iCartItem).getImage());
                            order.setProductName(((GoodsBean)iCartItem).getGoods_name());
                            order.setOrderAt(currentTime);
                            order.setQuantity(((GoodsBean)iCartItem).getGoods_amount());
                            order.setTotalPrice(((GoodsBean)iCartItem).getGoods_price()*((GoodsBean)iCartItem).getGoods_amount());
                            oderList.add(order);
                        }
                }}

                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    intent.putParcelableArrayListExtra("order_list", (ArrayList<? extends Parcelable>) oderList);
                startActivity(intent);
            }
            }
        }}


    private List<CartItemBean> getData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.55.93:1337/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService myApi = retrofit.create(ApiService.class);
        final int productCount = 5;

  /*      for (int i = 0; i < productCount; ++i) {
            final int productIndex = i + 1;
            myApi.getProduct(productIndex).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product product = response.body();
                    Attributes attributes = product.getData().getAttributes();
                    String brandName = attributes.getBrandName();

                    if (!brandNameList.contains(brandName)) {
                        brandNameList.add(brandName);
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    t.printStackTrace();
                }
            });*/
        brandNameList.add("Coolmate");
        brandNameList.add("Yame");
        brandNameList.add("LADOS");

            for(String brandname : brandNameList)
            {
                for(i = 1; i <= productCount; ++i)
                {myApi.getProduct(i).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response)
                    {   Product product = response.body();
                        Attributes attributes = product.getData().getAttributes();
                        if(!NameList.contains(attributes.getBrandName())&& brandname.equals(attributes.getBrandName())){
                            ShopBean shopBean = new ShopBean();
                            shopBean.setShop_name(brandname);
                            shopBean.setItemType(CartItemBean.TYPE_GROUP);
                            NameList.add(attributes.getBrandName());
                            cartItemBeans.add(shopBean);
                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setGoods_name(attributes.getProductName());
                            goodsBean.setItemType(CartItemBean.TYPE_CHILD);
                            goodsBean.setItemId(productIndex);
                            goodsBean.setGoods_price(Double.valueOf(attributes.getPrice()));
                            goodsBean.setImage(attributes.getImageURL());
                            goodsBean.setGroupId(count);
                            cartItemBeans.add(goodsBean);
                        }
                        else if(NameList.contains(attributes.getBrandName())&& brandname.equals(attributes.getBrandName()))
                        {   GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setGoods_name(attributes.getProductName());
                            goodsBean.setItemType(CartItemBean.TYPE_CHILD);
                            goodsBean.setItemId(productIndex);
                            goodsBean.setGoods_price(Double.valueOf(attributes.getPrice()));
                            goodsBean.setImage(attributes.getImageURL());
                            goodsBean.setGroupId(count);
                            cartItemBeans.add(goodsBean);
                        }
                    }
                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        t.printStackTrace();
                    }


                });
               }
            }

        return cartItemBeans;

    }

}