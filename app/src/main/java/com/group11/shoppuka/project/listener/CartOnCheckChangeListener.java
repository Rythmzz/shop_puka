package com.group11.shoppuka.project.listener;

import androidx.recyclerview.widget.RecyclerView;

import com.group11.shoppuka.project.bean.ICartItem;
import com.group11.shoppuka.project.bean.IGroupItem;
import com.group11.shoppuka.project.adapter.CartAdapter;
import com.group11.shoppuka.project.adapter.ParseHelper;

import java.util.List;

public abstract class CartOnCheckChangeListener implements OnCheckChangeListener, OnItemChangeListener {

    RecyclerView recyclerView;
    CartAdapter cartAdapter;

    public CartOnCheckChangeListener(RecyclerView recyclerView, CartAdapter adapter) {
        this.recyclerView = recyclerView;
        this.cartAdapter = adapter;
    }

    @Override
    public void onCheckedChanged(List<ICartItem> beans, int position, boolean isChecked, int itemType) {
        switch (itemType) {
            case ICartItem.TYPE_GROUP:
                groupCheckChange(beans, position, isChecked);
                break;
            case ICartItem.TYPE_CHILD:
                childCheckChange(beans, position, isChecked);
                break;
            default:
                break;
        }
    }

    @Override
    public void groupCheckChange(List<ICartItem> beans, int position, boolean isChecked) {
        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && !recyclerView.isComputingLayout()) {
            beans.get(position).setChecked(isChecked);
            setChildCheck(beans, position, isChecked);
        }
    }

    /**
     * child
     *
     * @param beans
     * @param position  child position
     * @param isChecked
     */
    @Override
    public void childCheckChange(List<ICartItem> beans, int position, boolean isChecked) {

        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && !recyclerView.isComputingLayout()) {

            beans.get(position).setChecked(isChecked);

            IGroupItem groupBean = ParseHelper.getGroupBean(beans, position);
            List<ICartItem> childList = ParseHelper.getChildList(beans, position);

            if (!isChecked) {

                if (groupBean.isChecked()) {
                    int groupPosition = ParseHelper.getGroupPosition(beans,
                            position);
                    setGroupCheck(beans, groupPosition, false);
                    cartAdapter.notifyItemChanged(groupPosition, CartAdapter.PAYLOAD_CHECKBOX);
                }
            } else {
                for (int i = 0; i < childList.size(); i++) {

                    if (!childList.get(i).isChecked()) {
                        return;
                    }
                }

                int groupPosition = ParseHelper.getGroupPosition(beans, position);

                setGroupCheck(beans, groupPosition, true);
                cartAdapter.notifyItemChanged(groupPosition, CartAdapter.PAYLOAD_CHECKBOX);
            }
        }
    }

    private void setChildCheck(List<ICartItem> beans, int position, boolean isChecked) {
        for (int i = (position + 1); i < beans.size(); i++) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                break;
            } else if (beans.get(i).getItemType() == ICartItem.TYPE_CHILD) {
                if (beans.get(i).isChecked() != isChecked) {
                    beans.get(i).setChecked(isChecked);
                    cartAdapter.notifyItemChanged(i, CartAdapter.PAYLOAD_CHECKBOX);
                }
            }
        }
    }


    private void setGroupCheck(List<ICartItem> beans, int groupPosition, boolean isChecked) {
        beans.get(groupPosition).setChecked(isChecked);
    }
}