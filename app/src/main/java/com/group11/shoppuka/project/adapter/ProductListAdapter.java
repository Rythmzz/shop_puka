package com.group11.shoppuka.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.Product;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.Holder> {
    Product listProduct[];

    public ProductListAdapter(Product listProduct[]){
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imageView.setImageResource(listProduct[position].getImage());
        holder.textView.setText(listProduct[position].getName());
        holder.textView1.setText(listProduct[position].getPrice());
    }

    @Override
    public int getItemCount() {
        return listProduct.length;
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        TextView textView1;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
            textView = itemView.findViewById(R.id.textViewNameProduct);
            textView1 = itemView.findViewById(R.id.textViewPriceProduct);
        }
    }
}
