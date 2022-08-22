package com.example.team3.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.team3.DetailsActivity;
import com.example.team3.R;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Product;
import com.example.team3.utils.FirestoreUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, priceTextView, descriptionTextView;
        public ImageView imageView;
        private CardView layout;
        private LikeButton likeButton;

        public ViewHolder(View view) {
            super(view);
            layout  = view.findViewById(R.id.card_layout);
            nameTextView  = view.findViewById(R.id.product_name);
            priceTextView = view.findViewById(R.id.product_price);
            descriptionTextView = view.findViewById(R.id.product_artist);
            imageView = view.findViewById(R.id.product_image);
            likeButton = view.findViewById(R.id.card_like_button);
        }
    }

    private List<IProduct> products;
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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IProduct product = products.get(position);

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(product.getPrice() + " USD");
        holder.descriptionTextView.setText(product.getArtist());
        holder.likeButton.setLiked(product.getLiked());

        String firstImageUrl = product.getImages().get(0);
        if (firstImageUrl != null) {
            Glide.with(context).load(firstImageUrl).into(holder.imageView);
        }

        holder.layout.setOnClickListener(v -> startDetailsActivity(v, product));
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addProductToFavourites(product);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                removeProductFromFavourites(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Helper method to add product to favourites collection.
     * @param product IProduct model.
     */
    private void addProductToFavourites(IProduct product) {
        product.setLiked(true);

        CollectionReference ref = FirestoreUtils.getCollectionReference(product.getCategory());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ref.document(String.valueOf(product.getId())).update("liked", true)
                .addOnSuccessListener(unused ->
                        db.collection("Favourites")
                                .document(String.valueOf(product.getId())).set(product)
                                .addOnFailureListener(Throwable::printStackTrace)
                );
    }

    /**
     * Helper method to remove product from favourites collection.
     * @param product IProduct model.
     */
    private void removeProductFromFavourites(IProduct product) {
        product.setLiked(false);

        CollectionReference ref = FirestoreUtils.getCollectionReference(product.getCategory());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ref.document(String.valueOf(product.getId())).update("liked", false)
                .addOnSuccessListener(unused ->
                        db.collection("Favourites")
                                .document(String.valueOf(product.getId())).delete()
                                .addOnFailureListener(Throwable::printStackTrace)
                );
    }

    /**
     * Helper method to package intent to send to the details activity.
     * @param v card view object.
     * @param product product details model.
     */
    private void startDetailsActivity(View v, IProduct product) {
        Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
        detailsIntent.putExtra("name", product.getName());
        detailsIntent.putExtra("artist", product.getArtist());
        detailsIntent.putExtra("description", product.getDescription());
        detailsIntent.putExtra("price", String.valueOf(product.getPrice()));
        detailsIntent.putExtra("image", product.getImages().get(0));
        detailsIntent.putExtra("type", product.getCategory());
        detailsIntent.putExtra("id", String.valueOf(product.getId()));
        v.getContext().startActivity(detailsIntent);
    }
}
