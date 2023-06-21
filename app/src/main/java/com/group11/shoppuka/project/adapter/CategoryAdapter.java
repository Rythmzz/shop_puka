package com.group11.shoppuka.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.category.CategoryResponse;
import com.group11.shoppuka.project.application.MyApplication;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    CategoryResponse categoryResponseList;

    public CategoryAdapter(CategoryResponse categoryResponseList){
        this.categoryResponseList = categoryResponseList;
    }

    public void setCategoryResponseList(CategoryResponse categoryResponseList) {
        this.categoryResponseList = categoryResponseList;
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

    }

    @Override
    public int getItemCount() {
        if (categoryResponseList.getData() != null){
            return categoryResponseList.getData().size();
        }
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCategory);
            textView = itemView.findViewById(R.id.textViewCategory);
        }
    }
}
