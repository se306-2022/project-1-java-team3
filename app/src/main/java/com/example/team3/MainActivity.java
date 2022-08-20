package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.team3.adapters.ProductAdapter;
import com.example.team3.data.DataProvider;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private class ViewHolder {
        public RecyclerView recyclerView;
        public ProgressBar progressBar;
        public ViewHolder() {
            recyclerView = findViewById(R.id.recycler_view);
            progressBar = findViewById(R.id.featured_progress_bar);
        }
    }

    private ViewHolder vh;
    private ProductAdapter adapter;
    private List<IProduct> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vh = new ViewHolder();

        productsList = new LinkedList<>();
        adapter = new ProductAdapter(productsList);

        vh.recyclerView.setAdapter(adapter);

        fetchProductsData();
    }

    private void fetchProductsData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Paintings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productsList.addAll(task.getResult().toObjects(Product.class));
                adapter.notifyDataSetChanged();

                vh.progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "Loading products successful.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void seedData() {
        DataProvider dp = new DataProvider(this);
        dp.seedData();
    }
}