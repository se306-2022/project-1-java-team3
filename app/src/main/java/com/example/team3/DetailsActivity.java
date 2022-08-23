package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.team3.adapters.ProductAdapter;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.utils.FirestoreUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private class ViewHolder {
        public TextView productName;
        public TextView productArtist;
        public TextView productDesc;
        public TextView productPrice;
        public ImageView productImage;

        public ViewHolder() {
            productName = findViewById(R.id.productName);
            productArtist = findViewById(R.id.productCreator);
            productDesc = findViewById(R.id.productDesc);
            productPrice = findViewById(R.id.productPrice);
            productImage = findViewById(R.id.productImage);
        }
    }

    private DetailsActivity.ViewHolder vh;
    private Context context;
    private IProduct product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        product = (IProduct) getIntent().getSerializableExtra("product");

        vh = new ViewHolder();
        vh.productName.setText(product.getName());
        vh.productArtist.setText("Created by " + product.getArtist());
        vh.productDesc.setText(product.getDescription());
        vh.productPrice.setText(product.getPrice() + " USD");

        setImages();
        incrementViewCount();
    }

    private void setImages() {
        String imageUrl = product.getImages().get(0);
        Glide.with(this).load(imageUrl).into(vh.productImage);
        // TODO do for multiple images. Intents can hold array values I think.
    }

    public void incrementViewCount() {
        String type = product.getCategory();
        String documentId = String.valueOf(product.getId());

        CollectionReference ref = FirestoreUtils.getCollectionReference(type);
        ref.document(documentId).update("viewCount", FieldValue.increment(1));
    }

    public void showMain(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}