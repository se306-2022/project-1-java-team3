package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query.Direction;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.example.team3.adapters.ProductAdapter;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Photo;
import com.example.team3.models.product.Digital;

public class ListActivity extends AppCompatActivity {

    private class ViewHolder {
        public RecyclerView recyclerView;
        public ProgressBar progressBar;
        public Spinner priceSpinner;
        public Spinner themeSpinner;
        public Spinner colourSpinner;
        public TextView headerText;

        public ViewHolder() {
            recyclerView = findViewById(R.id.recycler_view);
            progressBar = findViewById(R.id.featured_progress_bar);
            headerText = findViewById(R.id.category_name);
            priceSpinner = findViewById(R.id.price_filter);
            themeSpinner = findViewById(R.id.theme_filter);
            colourSpinner = findViewById(R.id.colour_filter);
        }
    }

    private ViewHolder vh;
    private ProductAdapter adapter;
    private List<IProduct> productsList;
    private Filter priceFilter;
    private Filter themeFilter;
    private Filter colourFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Retrieve category
        Bundle extras = getIntent().getExtras();
        String category = extras.getString("key");

        vh = new ViewHolder();

        // Change Header text
        if (Objects.equals(category, "Photos")) {
            vh.headerText.setText(R.string.Photos);
        } else if (Objects.equals(category, "Paintings")) {
            vh.headerText.setText(R.string.Paintings);
        } else {
            vh.headerText.setText(R.string.Digital);
        }

        // Initialising filters
        priceFilter = new Filter(vh.priceSpinner);
        themeFilter = new Filter(vh.themeSpinner);

        // Initialising product list in recycler view
        colourFilter = new Filter(vh.colourSpinner);
        productsList = new LinkedList<>();
        adapter = new ProductAdapter(productsList);
        vh.recyclerView.setAdapter(adapter);

        fetchProductsData(category);

        // Filter spinner listeners for changes - price is sort, theme and colour are filter
        vh.priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position>0){
                    String selectedItem = parentView.getItemAtPosition(position).toString();
                    fetchPriceSortedProductsData(category, selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        vh.themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position>0){
                    String selectedItem = parentView.getItemAtPosition(position).toString();
                    fetchProductsByFilter(category, "theme", selectedItem.toLowerCase());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        vh.colourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position>0){
                    String selectedItem = parentView.getItemAtPosition(position).toString();
                    fetchProductsByFilter(category, "mainColour", selectedItem.toLowerCase());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    /**
     * Populates product adapter with all products in the selected category
     *
     * @param  category  current category showing on list view
     */
    private void fetchProductsData(String category) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(category).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (category.equals("Painting")) {
                    productsList.addAll(task.getResult().toObjects(Painting.class));
                } else if (category.equals("Photo")) {
                    productsList.addAll(task.getResult().toObjects(Photo.class));
                } else {
                    productsList.addAll(task.getResult().toObjects(Digital.class));
                }

                // Dynamically setting filters based off of available products in category
                priceFilter.setFilterSpinner(this, R.array.price_filters);
                themeFilter.setFilterSpinnerDynamic(this, productsList, "theme");
                colourFilter.setFilterSpinnerDynamic(this, productsList, "colour");

                adapter.notifyDataSetChanged();

                vh.progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "Loading products successful.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Repopulates product adapter with products sorted in a given order
     *
     * @param  category  current category showing on list view
     * @param  order ascending or descending
     */
    private void fetchPriceSortedProductsData(String category, String order) {
        productsList.clear();
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setting the direction of sort
        Direction direction;
        if (order.equals("Price Asc")){
            direction = Direction.ASCENDING;
        } else {
            direction = Direction.DESCENDING;
        }

        db.collection(category).orderBy("price", direction).limit(3).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (category.equals("Painting")) {
                    productsList.addAll(task.getResult().toObjects(Painting.class));
                } else if (category.equals("Photo")) {
                    productsList.addAll(task.getResult().toObjects(Photo.class));
                } else {
                    productsList.addAll(task.getResult().toObjects(Digital.class));
                }

                adapter.notifyDataSetChanged();

                vh.progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "Loading products successful.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Repopulates product adapter with products filtered by given filter
     *
     * @param  category  current category showing on list view
     * @param  filterType which field to filter on - theme, mainColour
     * @param  filterValue the value to match
     */
    public void fetchProductsByFilter(String category, String filterType, String filterValue) {
        productsList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(category).whereEqualTo(filterType, filterValue).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (category.equals("Painting")) {
                    productsList.addAll(task.getResult().toObjects(Painting.class));
                } else if (category.equals("Photo")) {
                    productsList.addAll(task.getResult().toObjects(Photo.class));
                } else {
                    productsList.addAll(task.getResult().toObjects(Digital.class));
                }

                adapter.notifyDataSetChanged();

                vh.progressBar.setVisibility(View.GONE);
                if (productsList.size() > 0) {
                    Toast.makeText(getBaseContext(), "Filter successful.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "No Products Fit Filter", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "Filter failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showMain(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
