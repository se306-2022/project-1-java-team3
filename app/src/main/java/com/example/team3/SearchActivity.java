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
import com.example.team3.models.product.Digital;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Photo;
import com.example.team3.utils.SearchUtils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

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

        vh.searchBar.setQueryHint("Search All Products");

        productsList = new LinkedList<>();
        allProducts = new LinkedList<>();
        adapter = new ProductAdapter(productsList);
        vh.recyclerView.setAdapter(adapter);

        fetchAllProducts();

        vh.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                vh.searchBar.clearFocus();
                productsList.clear();
                productsList.addAll(SearchUtils.getProductsBySearch(allProducts, query));
                vh.progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                productsList.clear();
                productsList.addAll(SearchUtils.getProductsBySearch(allProducts, query));
                vh.progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        vh.backButton.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        });
    }

    /**
     * Retrieves products from all categories to be searched on (has to query all collections)
     */
    private void fetchAllProducts() {
        productsList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Paintings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productsList.addAll(task.getResult().toObjects(Painting.class));
                db.collection("Digital").get().addOnSuccessListener(queryDocumentSnapshotsDigital -> {
                    productsList.addAll(queryDocumentSnapshotsDigital.toObjects(Digital.class));
                    db.collection("Photos").get().addOnSuccessListener(queryDocumentSnapshotsPhotos -> {
                        productsList.addAll(queryDocumentSnapshotsPhotos.toObjects(Photo.class));
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