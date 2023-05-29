package com.group11.shoppuka.project.listener;

import com.group11.shoppuka.project.bean.ICartItem;

import java.util.List;

public interface OnChildCollapsingChangeListener {
    void onGroupItemClickCollapsibleChild(List<ICartItem> beans, int position);
}
