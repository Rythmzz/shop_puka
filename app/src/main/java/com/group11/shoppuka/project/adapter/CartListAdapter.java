package com.group11.shoppuka.project.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartData;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.view.product.DetailProductPageActivity;
import com.group11.shoppuka.project.viewmodel.CartViewModel;

import java.util.Objects;

import javax.inject.Inject;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.Holder> {
    private CartResponse cartResponse;
    private ProductResponse productResponse;

    private final CartViewModel cartViewModel;

    @Inject
    public CartListAdapter(CartResponse cartResponse, ProductResponse productResponse,  CartViewModel cartViewModel){
        this.cartResponse = cartResponse;
        this.productResponse = productResponse;
        this.cartViewModel = cartViewModel;
    }

    public void setCartResponse(CartResponse cartResponse) {
        this.cartResponse = cartResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    @NonNull
    @Override
    public CartListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new Holder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.Holder holder, int position) {
        try{
            holder.textView2.setVisibility(View.GONE);
            holder.textView1.setPaintFlags(holder.textView1.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            Product currentProductSelect = new Product();
            if (productResponse != null && cartResponse != null){
                for (Product product: productResponse.getData()){
                    if (cartResponse.getData().get(position).getAttributes().getIdProduct() == product.getAttributes().getIdProduct())
                        currentProductSelect = product;
                }
                int currentCountProduct = cartResponse.getData().get(position).getAttributes().getCount();
                int currentPriceProduct = (currentProductSelect.getAttributes().getPrice()*currentCountProduct);
                int currentSalePriceProduct = (currentProductSelect.getAttributes().getSalePrice());
                if (currentSalePriceProduct != 0) {
                    holder.textView1.setPaintFlags(holder.textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.textView1.setTextColor(Color.parseColor("#ACABAB"));
                    holder.textView2.setText(MyApplication.formatCurrency(String.valueOf(currentSalePriceProduct*currentCountProduct)) + " VNĐ");
                    holder.textView2.setVisibility(View.VISIBLE);
                }
                else {
                    holder.textView1.setTextColor(Color.parseColor("#cf052d"));
                }
                String url = MyApplication.localHost + currentProductSelect.getAttributes().getImageURL();
                Glide.with(holder.itemView.getContext()).load(url).into(holder.imageView);
                if (currentProductSelect.getAttributes().getName().length() <= 20)
                    holder.textView.setText(currentProductSelect.getAttributes().getName());
                else
                    holder.textView.setText((currentProductSelect.getAttributes().getName()).substring(0, Math.min(productResponse.getData().get(position).getAttributes().getName().length(), 20)) + "...");
                holder.textView1.setText(MyApplication.formatCurrency(String.valueOf(currentPriceProduct*currentCountProduct)) + " VNĐ");

                holder.tvCount.setText(String.valueOf(cartResponse.getData().get(position).getAttributes().getCount()));
                holder.btnAdd.setOnClickListener(view -> {
                    CartRequest cartRequest = new CartRequest();
                    CartData cartData = new CartData();
                    cartData.setIdProduct(cartResponse.getData().get(position).getAttributes().getIdProduct());
                    cartData.setPhoneNumber(cartResponse.getData().get(position).getAttributes().getPhoneNumber());
                    int totalPrice = cartResponse.getData().get(position).getAttributes().getTotalPrice();
                    cartData.setTotalPrice(totalPrice);
                    cartData.setCount(1);
                    cartRequest.setData(cartData);
                    cartViewModel.addCart(cartRequest);
                    cartViewModel.fetchListCart();
                    notifyItemChanged(position);
                });
                holder.btnSub.setOnClickListener(view -> {
                    CartResponse currentCartResponse = new CartResponse();
                    currentCartResponse.setData(Objects.requireNonNull(cartViewModel.getCartResponseMutableLiveData().getValue()).getData());
                    Cart currentCart = cartResponse.getData().get(position);
                    int fake;
                    if (currentCart.getAttributes().getIdResource().isEmpty()){
                        fake = currentCart.getId();
                        cartViewModel.deleteIdCart(currentCart.getId());
                        cartResponse.getData().remove(position);
                        notifyDataSetChanged();
                    }
                    else {
                        fake = cartResponse.getData().get(position).getId();
                        cartViewModel.deleteIdCart(currentCart.getAttributes().getIdResource().get(0));
                        cartResponse.getData().get(position).getAttributes().getIdResource().remove(0);

                    }
                    System.out.println("Xóa Thành Công ID RESOURCE: "+ fake);

                    cartViewModel.fetchListCart();
                    notifyItemChanged(position);
                });
                Product finalCurrentProductSelect = currentProductSelect;
                holder.relativeLayout.setOnClickListener(view -> {
                    Product product;
                    product = finalCurrentProductSelect;
                    Intent intent = new Intent(view.getContext(), DetailProductPageActivity.class);
                    intent.putExtra("product",product);
                    view.getContext().startActivity(intent);
                });
            }
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return cartResponse.getData().size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textView;
        TextView textView1;
        TextView textView2;
        FrameLayout frameLayout;

        ImageView btnAdd;
        ImageView btnSub;
        TextView tvCount;

        ConstraintLayout clNoCart;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
            textView = itemView.findViewById(R.id.textViewNameProduct);
            textView1 = itemView.findViewById(R.id.textViewPriceProduct);
            textView2 = itemView.findViewById(R.id.textViewSalePriceProduct);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            frameLayout = itemView.findViewById(R.id.frameLayoutMore);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnSub = itemView.findViewById(R.id.btnSub);
            tvCount = itemView.findViewById(R.id.numberCount);
            clNoCart = itemView.findViewById(R.id.csNotCart);
        }
    }
}
