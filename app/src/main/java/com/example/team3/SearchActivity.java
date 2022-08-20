package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team3.adapters.ProductAdapter;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public SearchView searchBar;

    private class ViewHolder {
        public RecyclerView recyclerView;
        public SearchView searchBar;
        public ProgressBar progressBar;

        public ViewHolder() {
            progressBar = findViewById(R.id.search_progress_bar);
            recyclerView = findViewById(R.id.recycler_view);
            searchBar = findViewById(R.id.search_bar);
        }
    }

    public ViewHolder vh;
    private ProductAdapter adapter;
    private List<IProduct> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        vh = new ViewHolder();

        productsList = new LinkedList<>();
        adapter = new ProductAdapter(productsList);
        vh.progressBar.setVisibility(View.GONE);

        vh.recyclerView.setAdapter(adapter);
//        getProductsBySearch("hi");

        vh.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                getProductsBySearch(query);
                vh.progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 0) {
                    getProductsBySearch(query);
                    vh.progressBar.setVisibility(View.VISIBLE);
                } else {
                    vh.progressBar.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void getProductsBySearch(String query) {
        productsList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Photos").whereGreaterThanOrEqualTo("name", query).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productsList.addAll(task.getResult().toObjects(Product.class));
                adapter.notifyDataSetChanged();

                vh.progressBar.setVisibility(View.GONE);

                if (productsList.size() == 0) {
                    Toast.makeText(getBaseContext(), "No matching products", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "Failed Search", Toast.LENGTH_LONG).show();
            }
        });
    }
}