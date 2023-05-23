package com.group11.shoppuka.project.bean;

import java.util.List;


public interface IGroupItem<CHILD extends IChildItem> extends ICartItem {
    List<CHILD> getChilds();

    void setChilds(List<CHILD> childs);
}
