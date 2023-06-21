package com.group11.shoppuka.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.order.Order;
import com.group11.shoppuka.project.model.order.OrderData;
import com.group11.shoppuka.project.model.order.OrderRequest;
import com.group11.shoppuka.project.model.order.OrderResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.viewmodel.OrderViewModel;

public class OrderListProgressAdapter extends RecyclerView.Adapter<OrderListProgressAdapter.Holder> {
    private OrderResponse orderResponse;
    private ProductResponse productResponse;

    public OrderListProgressAdapter(OrderResponse orderResponse, ProductResponse productResponse){
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
    public OrderListProgressAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_progress_item,parent,false);
        return new OrderListProgressAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListProgressAdapter.Holder holder, int position) {
        Order order = orderResponse.getData().get(position);
        holder.orderCode.setText("OrderCode#"+order.getAttributes().getOrderCode().toString().substring(0,7));
        holder.nameProduct.setText(productResponse.getData().get(order.getAttributes().getIdProduct()-1).getAttributes().getName());
        holder.totalPrice.setText(String.valueOf(order.getAttributes().getTotalPrice()) + " VNĐ");
        holder.address.setText(order.getAttributes().getAddress());
        holder.dateCreate.setText(order.getAttributes().getDateCreate());
        holder.quantity.setText("x"+order.getAttributes().getQuantity());
        String url = productResponse.getData().get(order.getAttributes().getIdProduct()-1).getAttributes().getImageURL();
        Glide.with(holder.itemView.getContext()).load(MyApplication.localHost+ url).into(holder.imageProduct);
        holder.phoneNumber.setText(order.getAttributes().getPhoneNumber());

        holder.checkSuccess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.checkSuccess.setChecked(false);
                    OrderData orderData = new OrderData();
                    orderData.setStatus(2);
                    orderData.setTotalPrice(order.getAttributes().getTotalPrice());
                    orderData.setQuantity(order.getAttributes().getQuantity());
                    orderData.setIdProduct(order.getAttributes().getIdProduct());
                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.setData(orderData);
                    OrderViewModel orderViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(OrderViewModel.class);
                    orderViewModel.updateData(order.getId(),orderRequest);
                    orderViewModel.fetchListData(1,1);
                }

            }
        });
        holder.checkFail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.checkFail.setChecked(false);
                    OrderData orderData = new OrderData();
                    orderData.setStatus(3);
                    orderData.setTotalPrice(order.getAttributes().getTotalPrice());
                    orderData.setQuantity(order.getAttributes().getQuantity());
                    orderData.setIdProduct(order.getAttributes().getIdProduct());
                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.setData(orderData);
                    OrderViewModel orderViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(OrderViewModel.class);
                    orderViewModel.updateData(order.getId(),orderRequest);
                    orderViewModel.fetchListData(1,1);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderResponse.getData() != null && productResponse.getData() != null  ? orderResponse.getData().size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView orderCode, nameProduct, totalPrice, address, phoneNumber, dateCreate, quantity;
        CheckBox checkSuccess, checkFail;
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
            checkSuccess = itemView.findViewById(R.id.btnConfirmProgress);
            checkFail = itemView.findViewById(R.id.btnCancelProgress);

        }
    }
}