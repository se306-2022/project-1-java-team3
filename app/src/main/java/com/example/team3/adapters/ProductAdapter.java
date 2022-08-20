package com.example.team3.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.team3.R;
import com.example.team3.models.product.IProduct;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, priceTextView, descriptionTextView;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            nameTextView  = view.findViewById(R.id.product_name);
            priceTextView = view.findViewById(R.id.product_price);
            descriptionTextView = view.findViewById(R.id.product_desc);
            imageView = view.findViewById(R.id.product_image);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }
    }

    private List<IProduct> products;
    private FirebaseFirestore firebaseFirestore;
    private Context context;

    public ProductAdapter(List<IProduct> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_view_item, parent, false);

        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IProduct product = products.get(position);

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.valueOf(product.getPrice()) + " USD");
        holder.descriptionTextView.setText(product.getDescription());

        String firstImageUrl = product.getImages().get(0);
        if (firstImageUrl != null) {
            Glide.with(context).load(firstImageUrl).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


}
