package com.group11.shoppuka.project.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group11.shoppuka.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    int images[];
    String text[];

    public CategoryAdapter(int images[], String text[]){
        this.images = images;
        this.text = text;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imageView.setImageResource(this.images[position]);
        holder.textView.setText(this.text[position]);

    }

    @Override
    public int getItemCount() {
        return images.length;
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
