package com.group11.shoppuka.project.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group11.shoppuka.project.bean.GoodsBean;
import com.group11.shoppuka.project.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private ImageLoader mImageLoader;
    private List<Order> ListOrder;

    public CheckoutItemAdapter(List<Order> orderList, Context context) {
        this.ListOrder = orderList;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view item cho RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Thiết lập dữ liệu cho từng view item
        Order order = ListOrder.get(position);
        Log.d("TESTHT",order.getProductName());
        holder.orderProductName.setText(order.getProductName());
        //Quy đổi tiền tệ
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(0);
        nf.setCurrency(Currency.getInstance("VND"));
        String formattedPrice = nf.format(order.getTotalPrice());
        holder.orderPrice.setText(formattedPrice);
        holder.orderType.setText("Color : "+"xanh");
        holder.orderQuantity.setText("Quantity : "+String.valueOf(order.getQuantity()));
        String imageUrl = order.getImageUrl();
        Glide.with(mContext)
               .load(imageUrl)
                .into(holder.orderImage);

    }

    private Context requireContext() {
        return null;
    }

    @Override
    public int getItemCount() {
        return ListOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderProductName,orderPrice,orderQuantity,orderType;
        ImageView orderImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderQuantity = itemView.findViewById(R.id.Quantity);
            orderProductName = itemView.findViewById(R.id.order_name);
            orderType = itemView.findViewById(R.id.order_type);
        }
    }
}
