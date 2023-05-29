package com.group11.shoppuka.project.bean;

public class CartItemBean implements ICartItem {

    private boolean isChecked = false;
    private int itemType;
    protected long itemId;
    private boolean isCollapsing = false;

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public long getItemId() {
        return itemId;
    }

    @Override
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean isCollapsing() {
        return isCollapsing;
    }

    @Override
    public void setCollapsing(boolean isCollapsing) {
        this.isCollapsing = isCollapsing;
    }


}
