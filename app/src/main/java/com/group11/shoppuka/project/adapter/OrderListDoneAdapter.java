package com.group11.shoppuka.project.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.order.Order;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;

public class OrderListDoneAdapter extends RecyclerView.Adapter<OrderListDoneAdapter.Holder> {
    private OrderResponse orderResponse;
    private ProductResponse productResponse;

    public OrderListDoneAdapter(OrderResponse orderResponse, ProductResponse productResponse){
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
    public OrderListDoneAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_done_item,parent,false);
        return new OrderListDoneAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListDoneAdapter.Holder holder, int position) {
        Order order = orderResponse.getData().get(position);
        holder.orderCode.setText("OrderCode#"+order.getAttributes().getOrderCode().toString().substring(0,7));
        holder.nameProduct.setText(productResponse.getData().get(order.getAttributes().getIdProduct()-1).getAttributes().getName());
        holder.totalPrice.setText(String.valueOf(order.getAttributes().getTotalPrice()) + " VNĐ");
        holder.address.setText(order.getAttributes().getAddress());
        switch (order.getAttributes().getStatus()){
            case 2: holder.checkStatus.setText("Đơn hàng giao thành công");
                break;
            case 3: holder.checkStatus.setText("Đơn hàng bị hủy");
                    holder.checkStatus.setTextColor(Color.RED);
                break;
        }
        holder.dateCreate.setText(order.getAttributes().getDateCreate());
        holder.quantity.setText("x"+order.getAttributes().getQuantity());
        String url = productResponse.getData().get(order.getAttributes().getIdProduct()-1).getAttributes().getImageURL();
        Glide.with(holder.itemView.getContext()).load(MyApplication.localHost+ url).into(holder.imageProduct);
        holder.phoneNumber.setText(order.getAttributes().getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return orderResponse.getData() != null && productResponse.getData() != null  ? orderResponse.getData().size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView orderCode, nameProduct, totalPrice, address, phoneNumber, dateCreate, quantity;
        TextView checkStatus;
        ImageView imageProduct;


        public Holder(@NonNull View itemView) {
            super(itemView);
            orderCode = itemView.findViewById(R.id.order_code);
            nameProduct = itemView.findViewById(R.id.order_name);
            totalPrice = itemView.findViewById(R.id.order_price);
            address = itemView.findViewById(R.id.tvAddress);
            phoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            quantity = itemView.findViewById(R.id.tvQuantity);
            dateCreate = itemView.findViewById(R.id.tvOrderDate);
            imageProduct = itemView.findViewById(R.id.order_image);
            checkStatus = itemView.findViewById(R.id.tvStatusOrder);

        }
    }
}