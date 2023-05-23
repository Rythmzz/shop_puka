package com.group11.shoppuka.project.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.ProductTest;


public class ProductListSalePriceAdapter extends RecyclerView.Adapter<ProductListSalePriceAdapter.Holder> {
    ProductTest listProduct[];
    private Context context;

    public ProductListSalePriceAdapter(ProductTest listProduct[]){
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_saleprice,parent,false);
        context = parent.getContext();
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imageView.setImageResource(listProduct[position].getImage());
        if (listProduct[position].getName().length() <= 15) holder.textView.setText(listProduct[position].getName());
    else holder.textView.setText((listProduct[position].getName()).substring(0,Math.min(listProduct[position].getName().length(),15))+"...");
        holder.textView1.setText((listProduct[position].getPrice()));
        holder.textView1.setPaintFlags(holder.textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(context, ProductDetail.class);
//                context.startActivity(intent);
            }
        });
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
