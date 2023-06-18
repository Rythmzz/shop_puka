package com.group11.shoppuka.project.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.other.MyApplication;
import com.group11.shoppuka.project.view.product.DetailProductPageActivity;

public class ProductListFilterAdapter extends RecyclerView.Adapter<ProductListFilterAdapter.Holder> {
    ProductResponse productResponse;


    String s;

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(String s) {
        this.s = s;
        notifyDataSetChanged();
    }
    public ProductListFilterAdapter(ProductResponse productResponse){
        this.productResponse = productResponse;

    }

    public void setProductResponse (ProductResponse productResponse){
        this.productResponse = productResponse;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
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
        holder.textView1.setText(String.valueOf((productResponse.getData().get(position).getAttributes().getPrice())) + "VNĐ");


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Product product = productResponse.getData().get(position);
                Intent intent = new Intent(view.getContext(), DetailProductPageActivity.class);
                intent.putExtra("product",product);
                view.getContext().startActivity(intent);

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

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
            textView = itemView.findViewById(R.id.textViewNameProduct);
            textView1 = itemView.findViewById(R.id.textViewPriceProduct);
            textView2 = itemView.findViewById(R.id.textViewSalePriceProduct);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
