package com.example.team3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.team3.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder> {

    private Context context;
    private List<String> images;

    public SliderAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.slider_image_view_item, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String imageUrl = images.get(position);
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    static class ViewHolder extends SliderViewAdapter.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_image_view);
        }
    }
}
