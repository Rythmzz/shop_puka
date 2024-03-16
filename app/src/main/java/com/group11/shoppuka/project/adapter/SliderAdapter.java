package com.group11.shoppuka.project.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.group11.shoppuka.R;
import com.group11.shoppuka.project.model.product.Product;
import com.group11.shoppuka.project.model.product.ProductResponse;
import com.group11.shoppuka.project.view.product.DetailProductPageActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    private final int[] images;

    private ProductResponse productResponse;

    public SliderAdapter(int[] images){
        this.images = images;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
       viewHolder.imageView.setImageResource(images[position]);

       viewHolder.imageView.setOnClickListener(v -> {
           switch (position){
               case 0 :
                   selectProduct(13,v);
                    break;
               case 1 :
                   selectProduct(14,v);
                   break;
               case 2 :
                   selectProduct(15,v);
                   break;
               case 3 :
                   selectProduct(16,v);
                   break;
               case 4 :
                   selectProduct(17,v);
                   break;
           }
       });
    }

    public void selectProduct(int position, View view){
        Product product = productResponse.getData().get(position);
        Intent intent = new Intent(view.getContext(), DetailProductPageActivity.class);
        intent.putExtra("product",product);
        view.getContext().startActivity(intent);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public static class Holder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public Holder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewSlider);
        }
    }
}
