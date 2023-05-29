package com.group11.shoppuka.project.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.cart.Cart;
import com.group11.shoppuka.project.model.cart.CartData;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.cart.CartResponse;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.view.DetailProductPageActivity;
import com.group11.shoppuka.project.viewmodel.CartViewModel;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.Holder> {
    CartResponse cartResponse;
    ProductResponse productResponse;

    public CartListAdapter(CartResponse cartResponse, ProductResponse productResponse){
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
    public CartListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.Holder holder, int position) {
        int positionCart = cartResponse.getData().get(position).getAttributes().getIdProduct() - 1;
        int count = cartResponse.getData().get(position).getAttributes().getCount();
        int price = (productResponse.getData().get(positionCart).getAttributes().getPrice()*count);
        int salePrice = (productResponse.getData().get(positionCart).getAttributes().getSalePrice());
        System.out.println(cartResponse.getData().get(position).getId() + " TOTAL ID");
        if (productResponse.getData().get(positionCart).getAttributes().getSalePrice() != 0) {
            holder.textView1.setPaintFlags(holder.textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView2.setText(String.valueOf(salePrice*count) + "VNĐ");
            holder.textView2.setVisibility(View.VISIBLE);
        }
        String url = MyApplication.localHost + productResponse.getData().get(positionCart).getAttributes().getImageURL();
        Glide.with(holder.itemView.getContext()).load(url).into(holder.imageView);
        if (productResponse.getData().get(positionCart).getAttributes().getName().length() <= 15)
            holder.textView.setText(productResponse.getData().get(positionCart).getAttributes().getName());
        else
            holder.textView.setText((productResponse.getData().get(positionCart).getAttributes().getName()).substring(0, Math.min(productResponse.getData().get(position).getAttributes().getName().length(), 15)) + "...");
        holder.textView1.setText(String.valueOf(price*count) + "VNĐ");

        holder.tvCount.setText(String.valueOf(cartResponse.getData().get(position).getAttributes().getCount()));
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartViewModel cartViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(CartViewModel.class);
                CartRequest cartRequest = new CartRequest();
                CartData cartData = new CartData();
                cartData.setIdProduct(cartResponse.getData().get(position).getAttributes().getIdProduct());
                cartData.setPhoneNumber(cartResponse.getData().get(position).getAttributes().getPhoneNumber());
                int totalPrice = cartResponse.getData().get(position).getAttributes().getTotalPrice();
                int count = cartResponse.getData().get(position).getAttributes().getCount();
                cartData.setTotalPrice(totalPrice);
                cartData.setCount(1);
                cartRequest.setData(cartData);
                cartViewModel.addCart(cartRequest,holder.itemView.getContext());
                int currentCount = Integer.valueOf(holder.tvCount.getText().toString());
                holder.tvCount.setText(String.valueOf(Integer.valueOf(currentCount)+1));
                holder.textView1.setText(String.valueOf(price*currentCount) + "VNĐ");
                holder.textView2.setText(String.valueOf(salePrice*currentCount)+"VNĐ");
            }
        });
        holder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartViewModel cartViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(CartViewModel.class);
                CartResponse currentCartResponse = new CartResponse();
                currentCartResponse.setData(cartViewModel.getCartResponseMutableLiveData().getValue().getData());
                Cart currentCart = cartResponse.getData().get(position);
                        if (currentCart.getAttributes().getIdResource().isEmpty()){
                            int fake =  currentCart.getId();
                            cartViewModel.deleteIdCart(currentCart.getId());
                            cartResponse.getData().remove(position);
                            notifyDataSetChanged();
                            System.out.println("Xóa Thành Công ID RESOURCE: "+ fake);
                        }
                        else {
                            for (int i = 0 ; i < cartResponse.getData().get(position).getAttributes().getIdResource().size();i++ ){
                                int fake =  cartResponse.getData().get(position).getId();
                                cartViewModel.deleteIdCart(currentCart.getAttributes().getIdResource().get(i));
                                cartResponse.getData().get(position).getAttributes().getIdResource().remove(i);
                                System.out.println("Xóa Thành Công ID RESOURCE: "+ fake);
                                break;
                            }
                        }

                int currentCount = Integer.valueOf(holder.tvCount.getText().toString())-1;
                holder.tvCount.setText(String.valueOf(currentCount));
                holder.textView1.setText(String.valueOf(currentCount*price) + "VNĐ");
                holder.textView2.setText(String.valueOf(currentCount*salePrice)+"VNĐ");
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Product product = productResponse.getData().get(positionCart);
                Intent intent = new Intent(view.getContext(), DetailProductPageActivity.class);
                intent.putExtra("product",product);
                view.getContext().startActivity(intent);
            }
        });
    }
    public void removeItem(int position) {
        if (position >= 0 && position < cartResponse.getData().size()) {
            cartResponse.getData().remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartResponse.getData().size());
        }
    }

    @Override
    public int getItemCount() {
        return cartResponse.getData() != null && productResponse.getData() != null ? cartResponse.getData().size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textView;
        TextView textView1;

        TextView textView2;

        FrameLayout frameLayout;

        ImageView btnAdd;
        ImageView btnSub;
        TextView tvCount;

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
        }
    }
}
