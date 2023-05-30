package com.group11.shoppuka.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.order.Order;
import com.group11.shoppuka.project.model.order.OrderData;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.Holder> {

    private OrderResponse orderResponse;
    private ProductResponse productResponse;

    public OrderListAdapter(OrderResponse orderResponse, ProductResponse productResponse){
        this.orderResponse = orderResponse;
        this.productResponse = productResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    public void setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

    @NonNull
    @Override
    public OrderListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new OrderListAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.Holder holder, int position) {
        Order order = orderResponse.getData().get(position);
        holder.orderCode.setText("OrderCode#"+order.getAttributes().getOrderCode().toString().substring(0,7));
        holder.nameProduct.setText(productResponse.getData().get(order.getAttributes().getIdProduct()+1).getAttributes().getName());
        holder.totalPrice.setText(String.valueOf(order.getAttributes().getTotalPrice()) + " VNĐ");
        holder.address.setText(order.getAttributes().getAddress());
        switch (order.getAttributes().getStatus()){
            case 0: holder.status.setText("Đơn hàng đang chờ xác nhận");
            break;
            case 1: holder.status.setText("Đơn hàng đang giao");
            break;
            case 2: holder.status.setText("Đơn hàng giao thành công");
            break;
            case 3: holder.status.setText("Đơn hàng bị hủy");
            break;
        }
        holder.dateCreate.setText(order.getAttributes().getDateCreate());
        holder.quantity.setText("x"+order.getAttributes().getQuantity());
        String url = productResponse.getData().get(order.getAttributes().getIdProduct()+1).getAttributes().getImageURL();
        Glide.with(holder.itemView.getContext()).load(MyApplication.localHost+ url).into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return orderResponse.getData() != null && productResponse.getData() != null  ? orderResponse.getData().size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView orderCode, nameProduct, totalPrice, address, status, dateCreate, quantity;
        ImageView imageProduct;


        public Holder(@NonNull View itemView) {
            super(itemView);
            orderCode = itemView.findViewById(R.id.order_code);
            nameProduct = itemView.findViewById(R.id.order_name);
            totalPrice = itemView.findViewById(R.id.order_price);
            address = itemView.findViewById(R.id.tvAddress);
            status = itemView.findViewById(R.id.tvStatus);
            quantity = itemView.findViewById(R.id.tvQuantity);
            dateCreate = itemView.findViewById(R.id.tvOrderDate);
            imageProduct = itemView.findViewById(R.id.order_image);

        }
    }
}
