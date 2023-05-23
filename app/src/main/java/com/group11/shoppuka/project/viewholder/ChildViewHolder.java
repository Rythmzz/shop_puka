package com.group11.shoppuka.project.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.group11.shoppuka.R;
import com.group11.shoppuka.project.bean.GoodsBean;

public abstract class ChildViewHolder extends CartViewHolder implements View.OnClickListener {
    public TextView textViewReduce;
    public TextView textView;
    public TextView textViewPrice;
    public TextView textViewNum;
    public TextView textViewAdd;
    public ImageView image;

    public ChildViewHolder(View itemView, int chekbox_id) {
        super(itemView, chekbox_id);

        textView = itemView.findViewById(R.id.tv);
        textViewPrice = itemView.findViewById(R.id.tv_price);
        textViewReduce = ((TextView) itemView.findViewById(R.id.tv_reduce));
        textViewNum = itemView.findViewById(R.id.tv_num);
        textViewAdd = itemView.findViewById(R.id.tv_add);
        image = itemView.findViewById(R.id.draw_goods);

        itemView.setOnClickListener(this);
        textViewReduce.setOnClickListener(this);
        textViewAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //nhấn vào sản phẩm trả về tên
            case R.id.item:
                Toast.makeText(v.getContext(), ((GoodsBean) mICartItem).getGoods_name(), Toast.LENGTH_SHORT).show();
                break;
                //Giảm số lượng sản phẩm
            case R.id.tv_reduce:
                int intValue = Integer.valueOf(textViewNum.getText().toString()).intValue();
                if (intValue > 1) {
                    intValue--;
                    textViewNum.setText(String.valueOf(intValue));
                    ((GoodsBean) mICartItem).setGoods_amount(intValue);
                    onNeedCalculate();
                }
                break;
                //tăng san pham va tinh toan lai
            case R.id.tv_add:
                int intValue2 = Integer.valueOf(textViewNum.getText().toString()).intValue();
                intValue2++;
                textViewNum.setText(String.valueOf(intValue2));
                ((GoodsBean) mICartItem).setGoods_amount(intValue2);
                onNeedCalculate();
                break;
            default:
                break;
        }
    }

    public abstract void onNeedCalculate();
}
