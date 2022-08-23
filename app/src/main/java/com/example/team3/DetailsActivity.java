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
import com.example.team3.adapters.SliderAdapter;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.utils.FirestoreUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.SliderView;

import java.util.Collection;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private class ViewHolder {
        public TextView productName;
        public TextView productArtist;
        public TextView productDesc;
        public TextView productPrice;
        public ImageView productImage;
        public SliderView sliderView;

        public ViewHolder() {
            productName = findViewById(R.id.productName);
            productArtist = findViewById(R.id.productCreator);
            productDesc = findViewById(R.id.productDesc);
            productPrice = findViewById(R.id.productPrice);
            productImage = findViewById(R.id.productImage);
            sliderView = findViewById(R.id.details_slider_view);
        }
    }

    private DetailsActivity.ViewHolder vh;
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
        vh.sliderView.setSliderAdapter(new SliderAdapter(this, product.getImages()));

        incrementViewCount();
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