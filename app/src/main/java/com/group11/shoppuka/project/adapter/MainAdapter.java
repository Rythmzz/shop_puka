package com.group11.shoppuka.project.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.bean.GoodsBean;
import com.group11.shoppuka.project.bean.ShopBean;
import com.group11.shoppuka.project.viewholder.CartViewHolder;
import com.group11.shoppuka.project.viewholder.ChildViewHolder;
import com.group11.shoppuka.project.viewholder.GroupViewHolder;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class MainAdapter extends CartAdapter<CartViewHolder> {

    public MainAdapter(Context context, List datas) {
        super(context, datas);
    }



    @Override
    protected CartViewHolder getGroupViewHolder(View itemView) {
        return (CartViewHolder) new GroupViewHolder(itemView, R.id.checkbox);
    }

    @Override
    protected CartViewHolder getChildViewHolder(View itemView) {
        return (CartViewHolder) (new ChildViewHolder(itemView, R.id.checkbox) {
            @Override
            public void onNeedCalculate() {
                if (onCheckChangeListener != null) {
                    onCheckChangeListener.onCalculateChanged(null);
                }
            }
        });
    }

    @Override
    protected int getChildItemLayout() {
        return R.layout.cart_item_child;
    }

    @Override
    protected int getGroupItemLayout() {
        return R.layout.cart_item_group;
    }

    //@Override
    //protected int getNormalItemLayout() {


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            childViewHolder.textView.setText(((GoodsBean) mData.get(position)).getGoods_name());
            //Quy đổi tiền tệ
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            nf.setMaximumFractionDigits(0);
            nf.setCurrency(Currency.getInstance("VND"));
            String formattedPrice = nf.format(((GoodsBean) mData.get(position)).getGoods_price());
            childViewHolder.textViewPrice.setText(formattedPrice);
            childViewHolder.textViewNum.setText(String.valueOf(((GoodsBean) mData.get(position)).getGoods_amount()));
            String imageUrl = ((GoodsBean) mData.get(position)).getImage();
            Glide.with(mContext)
                    .load(imageUrl)
                    .into(childViewHolder.image);
        } else if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            groupViewHolder.textView.setText(((ShopBean) mData.get(position)).getShop_name());
        }
           // normalViewHolder.textView.setText(mContext.getString(R.string.normal_tip_X,
           //         ((NormalBean) mData.get(position)).getMarkdownNumber()));

    }
}
