package com.example.team3;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team3.adapters.SliderAdapter;
import com.example.team3.models.product.Digital;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Photo;
import com.example.team3.utils.FirestoreUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.smarteist.autoimageslider.SliderView;

import java.text.NumberFormat;

public class DetailsActivity extends AppCompatActivity {

    private class ViewHolder {
        public TextView productName, productArtist, productDesc, productAddInfo, productPrice, yearTextView;
        public ImageView productImage;
        public SliderView sliderView;
        public LikeButton likeButton;

        public ViewHolder() {
            productName = findViewById(R.id.productName);
            productArtist = findViewById(R.id.productCreator);
            productDesc = findViewById(R.id.productDesc);
            productAddInfo = findViewById(R.id.productAddInfo);
            productPrice = findViewById(R.id.productPrice);
            productImage = findViewById(R.id.productImage);
            sliderView = findViewById(R.id.details_slider_view);
            likeButton = findViewById(R.id.details_like_button);
            yearTextView = findViewById(R.id.productYear);
        }
    }

    private ViewHolder vh;
    private IProduct product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get product object from Intent sent from previous activity
        product = (IProduct) getIntent().getSerializableExtra("product");

        // Declaring ViewHolder and setting text using product fields
        vh = new ViewHolder();
        vh.productName.setText(product.getName());
        vh.productArtist.setText("Created by " + product.getArtist());
        vh.productDesc.setText(product.getDescription());
        vh.yearTextView.setText(String.valueOf(product.getYear()));
        vh.productPrice.setText(NumberFormat.getInstance().format(product.getPrice()) + " USD");
        vh.sliderView.setSliderAdapter(new SliderAdapter(this, product.getImages()));
        vh.likeButton.setLiked(product.getLiked());

        if (product instanceof Painting) {
            vh.productAddInfo.setText("Medium: " + product.getMedium());
        } else if (product instanceof Photo) {
            vh.productAddInfo.setText("Captured with " + product.getCamera());
        } else if (product instanceof Digital) {
            vh.productAddInfo.setText("Blockchain: " + product.getBlockchain() + "\n" + "TokenId: " + product.getTokenId());
        }

        // Like button listener
        vh.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                product.setLiked(true);
                FirestoreUtils.addProductToFavourites(product);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                product.setLiked(false);
                FirestoreUtils.removeProductFromFavourites(product);
            }
        });

        incrementViewCount();
    }

    /**
     * Helper method to update the products view count in Firestore.
     */
    public void incrementViewCount() {
        String type = product.getCategory();
        String documentId = String.valueOf(product.getId());

        CollectionReference ref = FirestoreUtils.getCollectionReference(type);
        ref.document(documentId).update("viewCount", FieldValue.increment(1));
    }

    /**
     * Method to finish current activity and return to previous screen.
     */
    public void goBack(View v) {
        finish();
    }
}