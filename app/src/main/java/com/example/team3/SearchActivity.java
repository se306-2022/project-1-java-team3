package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.team3.adapters.ProductAdapter;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private class ViewHolder {
        public RecyclerView recyclerView;
        public SearchView searchBar;
        public ProgressBar progressBar;
        public ImageButton backButton;

        public ViewHolder() {
            progressBar = findViewById(R.id.search_progress_bar);
            recyclerView = findViewById(R.id.recycler_view);
            searchBar = findViewById(R.id.search_bar);
            backButton = findViewById(R.id.back_button);
        }
    }

    public ViewHolder vh;
    private ProductAdapter adapter;
    private List<IProduct> productsList;
    private List<IProduct> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        vh = new ViewHolder();

        productsList = new LinkedList<>();
        allProducts = new LinkedList<>();
        adapter = new ProductAdapter(productsList);
        vh.recyclerView.setAdapter(adapter);

        fetchAllProducts();

        vh.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getProductsBySearch(allProducts, query);
                vh.searchBar.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                getProductsBySearch(allProducts, query);
                return false;
            }
        });

        vh.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getProductsBySearch(List<IProduct> productsToSearch, String query) {
        productsList.clear();
        query = query.trim().toLowerCase();
        for (IProduct product : productsToSearch) {
            if (product.getName().toLowerCase().contains(query)) {
                productsList.add(product);
            }
        }
        vh.progressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }


    private void fetchAllProducts() {
        productsList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Paintings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productsList.addAll(task.getResult().toObjects(Product.class));
                db.collection("Digital").get().addOnSuccessListener(queryDocumentSnapshotsDigital -> {
                    productsList.addAll(queryDocumentSnapshotsDigital.toObjects(Product.class));
                    db.collection("Photos").get().addOnSuccessListener(queryDocumentSnapshotsPhotos -> {
                        productsList.addAll(queryDocumentSnapshotsPhotos.toObjects(Product.class));
                        allProducts.addAll(productsList);
                        adapter.notifyDataSetChanged();
                        vh.progressBar.setVisibility(View.GONE);
                    });
                });
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

}