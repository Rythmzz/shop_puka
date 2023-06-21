package com.group11.shoppuka.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.application.MyApplication;



public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.ViewHolder> {


    private CartResponse cartResponse;
    private ProductResponse productResponse;

    public CheckoutItemAdapter(CartResponse cartResponse, ProductResponse productResponse) {
        this.cartResponse = cartResponse;
        this.productResponse = productResponse;
    }

    public void setCartResponse(CartResponse cartResponse) {
        this.cartResponse = cartResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view item cho RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_manage_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int positionCart = cartResponse.getData().get(position).getAttributes().getIdProduct() - 1;
        int count = cartResponse.getData().get(position).getAttributes().getCount();

        String url = productResponse.getData().get(positionCart).getAttributes().getImageURL();
        Glide.with(holder.itemView.getContext()).load(MyApplication.localHost + url).into(holder.orderImage);
        holder.orderPrice.setText(String.valueOf(cartResponse.getData().get(position).getAttributes().getTotalPrice() * count) + " VNĐ");
        holder.orderQuantity.setText(String.valueOf(count));
        holder.orderProductName.setText(productResponse.getData().get(positionCart).getAttributes().getName());

    }

    @Override
    public int getItemCount() {
        return cartResponse.getData() != null ? cartResponse.getData().size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderProductName,orderPrice,orderQuantity;
        ImageView orderImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderQuantity = itemView.findViewById(R.id.Quantity);
            orderProductName = itemView.findViewById(R.id.order_name);
        }
    }
}
