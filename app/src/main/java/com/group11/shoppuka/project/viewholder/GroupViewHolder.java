package com.group11.shoppuka.project.viewholder;

import android.view.View;
import android.widget.TextView;

import com.group11.shoppuka.R;

public class GroupViewHolder extends CartViewHolder {
    public TextView textView;

    public GroupViewHolder(View itemView, int chekbox_id) {
        super(itemView, chekbox_id);
        textView = itemView.findViewById(R.id.tv);
    }
}
