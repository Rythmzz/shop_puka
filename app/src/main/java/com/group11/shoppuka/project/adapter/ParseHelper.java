package com.group11.shoppuka.project.adapter;

import com.group11.shoppuka.project.bean.ICartItem;
import com.group11.shoppuka.project.bean.IChildItem;
import com.group11.shoppuka.project.bean.IGroupItem;

import java.util.ArrayList;
import java.util.List;

public class ParseHelper {

    /**/
    public static IGroupItem<IChildItem> getGroupBean(List<ICartItem> beans, int childPosition) {
        for (int i = childPosition; i >= 0; i--) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                return ((IGroupItem) beans.get(i));
            }
        }
        return null;
    }


    public static List<ICartItem> getChildList(List<ICartItem> beans, int position) {
        List<ICartItem> childList = new ArrayList<>();
        for (int i = position; i < beans.size(); i++) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                break;
            } else if (beans.get(i).getItemType() == ICartItem.TYPE_CHILD) {
                childList.add(beans.get(i));
            }
        }

        for (int i = position - 1; i >= 0; i--) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                break;
            } else if (beans.get(i).getItemType() == ICartItem.TYPE_CHILD) {
                childList.add(beans.get(i));
            }
        }

        return childList;
    }


    public static int getGroupPosition(List<ICartItem> beans, int childPosition) {
        int groupPosition = 0;
        for (int i = childPosition; i >= 0; i--) {
            if (beans.get(i).getItemType() == ICartItem.TYPE_GROUP) {
                groupPosition = i;
                break;
            }
        }
        return groupPosition;
    }
}
