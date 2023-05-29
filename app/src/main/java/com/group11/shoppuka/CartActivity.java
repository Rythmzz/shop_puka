package com.group11.shoppuka;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.group11.shoppuka.project.CheckoutFragment;
import com.group11.shoppuka.project.bean.CartItemBean;
import com.group11.shoppuka.project.bean.GoodsBean;
import com.group11.shoppuka.project.bean.ICartItem;
import com.group11.shoppuka.project.bean.ShopBean;
import com.group11.shoppuka.project.listener.CartOnCheckChangeListener;
import com.group11.shoppuka.project.adapter.MainAdapter;
import com.group11.shoppuka.project.model.Order;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView mTvTitle;
    private TextView mTvEdit;
    private CheckBox mCheckBoxAll;
    private TextView mTvTotal;
    private Button mBtnSubmit;
    ArrayList<CartItemBean> cartItemBeans = new ArrayList<>();
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
                //CheckoutActivity checkoutActivity = new CheckoutActivity();
                //checkoutActivity.setListOrder(oderList);
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    intent.putParcelableArrayListExtra("order_list", (ArrayList<? extends Parcelable>) oderList);
                startActivity(intent);
            }
            }
        }}

    /*public void firebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartCollectionRef = db.collection("cart");
        DocumentReference document = cartCollectionRef.document("pP3gqPQkyB3Xp302LdLs");
        CollectionReference cartColl = document.collection("Coolmate");
        cartColl.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    Log.d("TEST", querySnapshot.toString());
                    if (querySnapshot != null) {
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                        for (DocumentSnapshot document : documents) {

                            ShopBean shopBean = new ShopBean();
                            shopBean.setShop_name("Coolmate");
                            shopBean.setItemType(CartItemBean.TYPE_GROUP);
                            cartItemBeans.add(shopBean);
                            // Thực hiện các hoạt động khác với đối tượng shopBean
                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setGoods_name(document.getString("Name"));
                            goodsBean.setImage(document.getString("Image"));
                            Log.d("TEST",document.toString());
                            goodsBean.setItemType(CartItemBean.TYPE_CHILD);
                            goodsBean.setItemId(20);
                            goodsBean.setGoods_price(Double.valueOf(document.getString("Price ")));
                            goodsBean.setGroupId(1);
                            cartItemBeans.add(goodsBean);
                        }

                    }
                } else {
                    // Xảy ra lỗi khi truy vấn dữ liệu
                }

            }
        });


    }*/

    private List<CartItemBean> getData() {

        //firebase();

        ShopBean shopBean1= new ShopBean();
        shopBean1.setShop_name("Coolmate");
        shopBean1.setItemType(CartItemBean.TYPE_GROUP);
        cartItemBeans.add(shopBean1);

        GoodsBean goodsBean1 = new GoodsBean();
        goodsBean1.setGoods_name("Áo thun MARVEL Craft Logo Graphic");
        goodsBean1.setItemType(CartItemBean.TYPE_CHILD);
        goodsBean1.setItemId(21);
        goodsBean1.setGoods_price(250000.);
        goodsBean1.setImage("https://media.coolmate.me/cdn-cgi/image/quality=80,format=auto/uploads/November2022/ao-thun-marvel-craft-logo-graphic-nau-1_84.jpg");
        goodsBean1.setGroupId(1);
        cartItemBeans.add(goodsBean1);

        GoodsBean goodsBean2 = new GoodsBean();
        goodsBean2.setGoods_name("Áo sát nách thể thao nam Dri-Breathe thoáng mát");
        goodsBean2.setItemType(CartItemBean.TYPE_CHILD);
        goodsBean2.setItemId(21);
        goodsBean2.setGoods_price(189000.);
        goodsBean2.setImage("https://media.coolmate.me/cdn-cgi/image/quality=80,format=auto/uploads/May2023/drixam.jpg");
        goodsBean2.setGroupId(1);
        cartItemBeans.add(goodsBean2);
        cartItemBeans.add(goodsBean2);
        cartItemBeans.add(goodsBean2);

        return cartItemBeans;

    }
}