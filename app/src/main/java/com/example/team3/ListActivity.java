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

import java.util.Collections;
import java.util.HashMap;
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
    private List<IProduct> allProducts;
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
        colourFilter = new Filter(vh.colourSpinner);

        // Initialising product list in recycler view
        productsList = new LinkedList<>();
        allProducts = new LinkedList<>();
        adapter = new ProductAdapter(productsList);
        vh.recyclerView.setAdapter(adapter);

        fetchProductsData(category);

        // Filter spinner listeners for changes - price is sort, theme and colour are filter
        vh.priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position>0){
                    sortProductList(vh.priceSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                HashMap<String, String> filters = new HashMap<>();
                String sort="";

                if (!vh.priceSpinner.getSelectedItem().toString().equals(getString(R.string.price_title))) {
                    sort = vh.priceSpinner.getSelectedItem().toString();
                }

                if (!vh.themeSpinner.getSelectedItem().toString().equals(getString(R.string.theme_title))) {
                    filters.put("theme", vh.themeSpinner.getSelectedItem().toString());
                }

                if (!vh.colourSpinner.getSelectedItem().toString().equals(getString(R.string.colour_title))) {
                    filters.put("mainColour", vh.colourSpinner.getSelectedItem().toString());
                }

                // If filters have been set and then reset, will repopulate products list with initial products loading
                if (filters.isEmpty() && (!productsList.equals(allProducts))) {
                    productsList.clear();
                    productsList.addAll(allProducts);
                    if (sort.length()>0) {
                        sortProductList(sort);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else if (!filters.isEmpty()) {
                    fetchProductsByFilterSet(filters); // Will filter based on theme and colour
                    if (sort.length()>0) {
                        sortProductList(sort);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        };

        vh.themeSpinner.setOnItemSelectedListener(filterListener);
        vh.colourSpinner.setOnItemSelectedListener(filterListener);

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
                colourFilter.setFilterSpinnerDynamic(this, productsList, "mainColour");

                // Saving an instance of all the products for filtering
                allProducts.addAll(productsList);

                adapter.notifyDataSetChanged();

                vh.progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "Loading products successful.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sortProductList(String direction) {
        Collections.sort(productsList,
                (p1, p2) -> p1.getPrice() - p2.getPrice());

        if (direction.equals(getString(R.string.desc))){
            Collections.reverse(productsList);
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * Repopulates product adapter with products filtered
     *
     * @param  filters  hashmap of <filterType, filterValue>
     */
    private void fetchProductsByFilterSet(HashMap<String,String> filters) {
        List<IProduct> filteredProducts = new LinkedList<>();
        productsList.clear(); // Clears the adapter

        boolean multipleFilters = (filters.size() > 1);

        // If multiple filters, will combine filters (filter a && filter b)
        if (multipleFilters) {
            List<IProduct> tempFilteredProducts = filterByTheme(allProducts, filters.get("theme"));
            filteredProducts = filterByColour(tempFilteredProducts, filters.get("mainColour"));
        } else if (((String)filters.keySet().toArray()[0]).equals("theme")){
            filteredProducts = filterByTheme(allProducts, filters.get("theme"));
        } else if (((String)filters.keySet().toArray()[0]).equals("mainColour")){
            filteredProducts = filterByColour(allProducts, filters.get("mainColour"));
        }

        // Repopulating adapter
        productsList.addAll(filteredProducts);
        adapter.notifyDataSetChanged();

        if (productsList.size() == 0) {
            Toast.makeText(getBaseContext(), "No products match filter", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Filters list of products by theme
     *
     * @param  listToFilter  list of products to apply filter on>
     * @param  filterValue  value to filter on>
     * @return List<IProduct> filtered list
     */
    private List<IProduct> filterByTheme(List<IProduct> listToFilter, String filterValue) {
        filterValue = filterValue.toLowerCase();
        List<IProduct> filteredList = new LinkedList<>();
        for (IProduct product : listToFilter) {
            if (product.getTheme().equals(filterValue)) {
                filteredList.add(product);
            }
        }

        return filteredList;
    }

    /**
     * Filters list of products by theme
     *
     * @param  listToFilter  list of products to apply filter on>
     * @param  filterValue  value to filter on>
     * @return List<IProduct> filtered list
     */
    private List<IProduct> filterByColour(List<IProduct> listToFilter, String filterValue) {
        filterValue = filterValue.toLowerCase();
        List<IProduct> filteredList = new LinkedList<>();
        for (IProduct product : listToFilter) {
            if (product.getMainColour().equals(filterValue)) {
                filteredList.add(product);
            }
        }

        return filteredList;
    }

    public void showMain(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
