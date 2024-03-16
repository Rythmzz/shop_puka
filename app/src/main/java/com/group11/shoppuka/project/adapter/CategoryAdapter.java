package com.group11.shoppuka.project.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.application.MyApplication;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.view.product.AllProductListPageActivity;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    private CategoryResponse categoryResponseList;

    private ProductResponse productResponse;

    public CategoryAdapter(CategoryResponse categoryResponseList){
        this.categoryResponseList = categoryResponseList;
    }

    public void setCategoryResponseList(CategoryResponse categoryResponseList) {
        this.categoryResponseList = categoryResponseList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String url = MyApplication.localHost + categoryResponseList.getData().get(position).getAttributes().getImageURL();
        Glide.with(holder.itemView.getContext()).load(url).into(holder.imageView);
        holder.textView.setText(categoryResponseList.getData().get(position).getAttributes().getName());

        holder.layout.setOnClickListener(v -> {
            ProductResponse currentProductResponseCategory = new ProductResponse();
            for (Product product : productResponse.getData()) {
                if (product.getAttributes().getIdCategory() == position+1) {
                    System.out.println("Add Product:"+product.getAttributes().getName());
                    currentProductResponseCategory.addProduct(product);


                }
            }
            selectProductList(currentProductResponseCategory,position,v);

        });
    }

    public void selectProductList(ProductResponse productResponse,int position, View view){
        Intent intent = new Intent(view.getContext(), AllProductListPageActivity.class);
        intent.putExtra("product_list",productResponse);
        intent.putExtra("category_id",position);
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (categoryResponseList.getData() != null) return categoryResponseList.getData().size();
        return 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        LinearLayout layout;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCategory);
            textView = itemView.findViewById(R.id.textViewCategory);
            layout = itemView.findViewById(R.id.layout_category);
        }
    }
}
