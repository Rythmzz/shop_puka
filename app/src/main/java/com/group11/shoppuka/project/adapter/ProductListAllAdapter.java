package com.group11.shoppuka.project.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.cart.CartData;
import com.group11.shoppuka.project.model.cart.CartRequest;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.view.product.DetailProductPageActivity;
import com.group11.shoppuka.project.viewmodel.CartViewModel;

import javax.inject.Inject;

public class ProductListAllAdapter extends RecyclerView.Adapter<ProductListAllAdapter.Holder> {

    private ProductResponse productResponse;


    CartViewModel cartViewModel;



    private String phoneNumber;

    @Inject
    public ProductListAllAdapter(ProductResponse productResponse, CartViewModel cartViewModel, String phoneNumber){
        this.productResponse = productResponse;
        this.cartViewModel = cartViewModel;
        this.phoneNumber = phoneNumber;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    @NonNull
    @Override
    public ProductListAllAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAllAdapter.Holder holder, int position) {
            if (productResponse.getData().get(position).getAttributes().getSalePrice() != 0) {
                holder.textView1.setPaintFlags(holder.textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.textView2.setText(String.valueOf((productResponse.getData().get(position).getAttributes().getSalePrice())) + "VNĐ");
            }
            String url = MyApplication.localHost + productResponse.getData().get(position).getAttributes().getImageURL();
            Glide.with(holder.itemView.getContext()).load(url).into(holder.imageView);
            if (productResponse.getData().get(position).getAttributes().getName().length() <= 15)
                holder.textView.setText(productResponse.getData().get(position).getAttributes().getName());
            else
                holder.textView.setText((productResponse.getData().get(position).getAttributes().getName()).substring(0, Math.min(productResponse.getData().get(position).getAttributes().getName().length(), 15)) + "...");
            holder.textView1.setText(MyApplication.formatCurrency(String.valueOf((productResponse.getData().get(position).getAttributes().getPrice()))) + " VNĐ");
        int currentSalePriceProduct = (productResponse.getData().get(position).getAttributes().getSalePrice());
        if (currentSalePriceProduct != 0) {
            holder.textView1.setPaintFlags(holder.textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView1.setTextColor(Color.parseColor("#ACABAB"));
            holder.textView2.setText(MyApplication.formatCurrency(String.valueOf(currentSalePriceProduct)) + " VNĐ");
            holder.textView2.setVisibility(View.VISIBLE);
        }
        else {
            holder.textView1.setTextColor(Color.parseColor("#cf052d"));
        }

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Product product = productResponse.getData().get(position);
                    Intent intent = new Intent(view.getContext(), DetailProductPageActivity.class);
                    intent.putExtra("product",product);
                    view.getContext().startActivity(intent);
                }
            });

        holder.btnCart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                CartData cartData = new CartData();
                cartData.setIdProduct(productResponse.getData().get(position).getId());
                System.out.println("Phone Number :"+phoneNumber);
                cartData.setPhoneNumber(phoneNumber);
                cartData.setCount(1);
                if (productResponse.getData().get(position).getAttributes().getSalePrice() != 0) {
                    cartData.setTotalPrice(productResponse.getData().get(position).getAttributes().getSalePrice());
                } else {
                    cartData.setTotalPrice(productResponse.getData().get(position).getAttributes().getPrice());
                }
                CartRequest cartRequest = new CartRequest();
                cartRequest.setData(cartData);

                cartViewModel.addCart(cartRequest);

                Toast.makeText(holder.itemView.getContext(),"Thêm sản phẩm vào giỏ hàng thành công!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productResponse.getData() != null ? productResponse.getData().size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textView;
        TextView textView1;

        TextView textView2;

        FrameLayout frameLayout;

        ImageView btnCart;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
            textView = itemView.findViewById(R.id.textViewNameProduct);
            textView1 = itemView.findViewById(R.id.textViewPriceProduct);
            textView2 = itemView.findViewById(R.id.textViewSalePriceProduct);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            frameLayout = itemView.findViewById(R.id.frameLayoutMore);
            btnCart = itemView.findViewById(R.id.add_cart);
        }
    }
}
