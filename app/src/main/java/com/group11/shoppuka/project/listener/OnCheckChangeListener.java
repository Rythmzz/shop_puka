package com.group11.shoppuka.project.listener;

import com.group11.shoppuka.project.bean.ICartItem;

import java.util.List;


public interface OnCheckChangeListener {

    void onCheckedChanged(List<ICartItem> beans, int position, boolean isChecked, int itemType);

    void onCalculateChanged(ICartItem cartItemBean);
}
