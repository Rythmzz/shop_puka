package com.group11.shoppuka.project.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
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

public class OrderListConfirmAdapter extends RecyclerView.Adapter<OrderListConfirmAdapter.Holder> {
    private OrderResponse orderResponse;
    private ProductResponse productResponse;

    private final OrderViewModel orderViewModel;

    public OrderListConfirmAdapter(OrderResponse orderResponse, ProductResponse productResponse, OrderViewModel orderViewModel){
        this.orderResponse = orderResponse;
        this.productResponse = productResponse;
        this.orderViewModel = orderViewModel;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderListConfirmAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_confirm_item,parent,false);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderListConfirmAdapter.Holder holder, int position) {

        Order order = orderResponse.getData().get(position);
        holder.orderCode.setText("OrderCode#"+ order.getAttributes().getOrderCode().substring(0,7));
        holder.nameProduct.setText(productResponse.getData().get(order.getAttributes().getIdProduct()-1).getAttributes().getName());
        holder.totalPrice.setText(MyApplication.formatCurrency(String.valueOf(order.getAttributes().getTotalPrice())) + " VNĐ");
        holder.address.setText(order.getAttributes().getAddress());
        holder.dateCreate.setText(order.getAttributes().getDateCreate());
        holder.quantity.setText("x"+order.getAttributes().getQuantity());
        String url = productResponse.getData().get(order.getAttributes().getIdProduct()-1).getAttributes().getImageURL();
        Glide.with(holder.itemView.getContext()).load(MyApplication.localHost+ url).into(holder.imageProduct);
        holder.phoneNumber.setText(order.getAttributes().getPhoneNumber());

        holder.checkSuccess.setOnClickListener(v -> {
            OrderData orderData = new OrderData();
            orderData.setStatus(1);
            orderData.setTotalPrice(order.getAttributes().getTotalPrice());
            orderData.setQuantity(order.getAttributes().getQuantity());
            orderData.setIdProduct(order.getAttributes().getIdProduct());
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setData(orderData);
//          OrderViewModel orderViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(OrderViewModel.class);
            orderViewModel.updateData(order.getId(),orderRequest);
            orderViewModel.fetchListData(0,0);
            holder.checkSuccess.setEnabled(false);
            holder.checkFail.setEnabled(false);

            Toast.makeText(holder.itemView.getContext(),"Đơn hàng đã được duyệt qua",Toast.LENGTH_SHORT).show();

        });
        holder.checkFail.setOnClickListener(v -> {
            OrderData orderData = new OrderData();
            orderData.setStatus(3);
            orderData.setTotalPrice(order.getAttributes().getTotalPrice());
            orderData.setQuantity(order.getAttributes().getQuantity());
            orderData.setIdProduct(order.getAttributes().getIdProduct());
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setData(orderData);
//            OrderViewModel orderViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(OrderViewModel.class);
            orderViewModel.updateData(order.getId(),orderRequest);
            orderViewModel.fetchListData(0,0);
            holder.checkSuccess.setEnabled(false);
            holder.checkFail.setEnabled(false);

            Toast.makeText(holder.itemView.getContext(),"Đơn hàng đã được hủy",Toast.LENGTH_SHORT).show();

        });
    }


    @Override
    public int getItemCount() {
        return orderResponse.getData() != null && productResponse.getData() != null  ? orderResponse.getData().size() : 0;
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView orderCode, nameProduct, totalPrice, address, phoneNumber, dateCreate, quantity;
        AppCompatButton checkSuccess, checkFail;
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
            checkSuccess = itemView.findViewById(R.id.btnConfirm);
            checkFail = itemView.findViewById(R.id.btnCancel);

        }
    }
}
