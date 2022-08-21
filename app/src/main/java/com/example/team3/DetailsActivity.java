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
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vh = new ViewHolder();
        vh.productName.setText(getIntent().getExtras().getString("name"));
        vh.productArtist.setText("Created by " + getIntent().getExtras().getString("artist"));
        vh.productDesc.setText(getIntent().getExtras().getString("description"));
        vh.productPrice.setText(getIntent().getExtras().getString("price") + " USD");

        setImages();

        db = FirebaseFirestore.getInstance();

        incrementViewCount();
    }

    private void setImages() {
        String imageUrl = getIntent().getExtras().getString("image");
        Glide.with(this).load(imageUrl).into(vh.productImage);
        // TODO do for multiple images. Intents can hold array values I think.
    }

    public void incrementViewCount() {
        CollectionReference ref;

        switch(getIntent().getExtras().getString("type")) {
            case "painting":
                ref = db.collection("Paintings");
                break;
            case "digital":
                ref = db.collection("Digital");
                break;
            case "photo":
                ref = db.collection("Photos");
                break;
            default:
                return;
        }

        String documentId = getIntent().getExtras().getString("id");
        ref.document(documentId).update("viewCount", FieldValue.increment(1));
    }

    public void showMain(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}