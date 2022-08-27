package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.team3.adapters.ProductAdapter;
import com.example.team3.models.product.Digital;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Photo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private class ViewHolder {
        public RecyclerView recyclerView;
        public ProgressBar progressBar;
        public ViewHolder() {
            recyclerView = findViewById(R.id.recycler_view);
            progressBar = findViewById(R.id.progress_bar);
        }
    }

    private ViewHolder vh;
    private ProductAdapter adapter;
    private List<IProduct> productsList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        vh = new ViewHolder();

        productsList = new LinkedList<>();
        adapter = new ProductAdapter(productsList);

        vh.recyclerView.setAdapter(adapter);

        fetchProductsData();
    }

    private void fetchProductsData() {
        db.collection("Paintings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productsList.addAll(task.getResult().toObjects(Painting.class));
                db.collection("Digital").get().addOnSuccessListener(queryDocumentSnapshotsDigital -> {
                    productsList.addAll(queryDocumentSnapshotsDigital.toObjects(Digital.class));
                    db.collection("Photos").get().addOnSuccessListener(queryDocumentSnapshotsPhotos -> {
                        productsList.addAll(queryDocumentSnapshotsPhotos.toObjects(Photo.class));

                        Collections.sort(productsList, (o1, o2) -> {
                            if(o1.getViewCount() == o2.getViewCount()) return 0;
                            return o1.getViewCount() > o2.getViewCount() ? -1 : 1;
                        });

                        adapter.notifyDataSetChanged();
                        vh.progressBar.setVisibility(View.GONE);
                    });
                });
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showPaintings(View v) {
        Intent listIntent = new Intent(this, ListActivity.class);
        listIntent.putExtra("key","Paintings");
        startActivity(listIntent);
    }

    public void showPhotos(View v) {
        Intent listIntent = new Intent(this, ListActivity.class);
        listIntent.putExtra("key","Photos");
        startActivity(listIntent);
    }

    public void showDigital(View v) {
        Intent listIntent = new Intent(this, ListActivity.class);
        listIntent.putExtra("key","Digital");
        startActivity(listIntent);
    }

    public void showFavourites(View v) {
        Intent listIntent = new Intent(this, ListActivity.class);
        listIntent.putExtra("key","Favourites");
        startActivity(listIntent);
    }

    public void showSearch(View v) {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        startActivity(searchIntent);
    }
}