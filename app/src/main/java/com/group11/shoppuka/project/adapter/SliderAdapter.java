package com.group11.shoppuka.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.group11.shoppuka.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    int images[];

    public SliderAdapter(int images[]){
        this.images = images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
       viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        ImageView imageView;
        public Holder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewSlider);
        }
    }
}
