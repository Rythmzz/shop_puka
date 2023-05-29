package com.group11.shoppuka.project.listener;

import com.group11.shoppuka.project.bean.ICartItem;

import java.util.List;

public interface OnItemChangeListener {

    void groupCheckChange(List<ICartItem> beans, int position, boolean isChecked);

    void childCheckChange(List<ICartItem> beans, int position, boolean isChecked);
}
