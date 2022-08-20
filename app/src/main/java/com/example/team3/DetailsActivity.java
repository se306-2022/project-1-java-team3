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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        vh = new ViewHolder();
        vh.productName.setText(getIntent().getExtras().getString("name"));
        vh.productArtist.setText("Created by " + getIntent().getExtras().getString("artist"));
        vh.productDesc.setText(getIntent().getExtras().getString("description"));
        vh.productPrice.setText(getIntent().getExtras().getString("price") + " USD");

    }

    public void showMain(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}