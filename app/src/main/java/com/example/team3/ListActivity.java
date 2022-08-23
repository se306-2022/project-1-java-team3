package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team3.utils.FilterUtils;
import com.example.team3.utils.SearchUtils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.example.team3.adapters.ProductAdapter;
import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Painting;
import com.example.team3.models.product.Photo;
import com.example.team3.models.product.Digital;
import com.example.team3.models.product.Product;

public class ListActivity extends AppCompatActivity {

    private class ViewHolder {
        public RecyclerView recyclerView;
        public ProgressBar progressBar;
        public Spinner priceSpinner;
        public Spinner themeSpinner;
        public Spinner colourSpinner;
        public TextView headerText;
        public SearchView searchBar;
        public ImageButton backButton;

        public ViewHolder() {
            recyclerView = findViewById(R.id.recycler_view);
            progressBar = findViewById(R.id.featured_progress_bar);
            headerText = findViewById(R.id.category_name);
            priceSpinner = findViewById(R.id.price_filter);
            themeSpinner = findViewById(R.id.theme_filter);
            colourSpinner = findViewById(R.id.colour_filter);
            searchBar = findViewById(R.id.search_bar);
            backButton = findViewById(R.id.back_button);
        }
    }

    private ViewHolder vh;
    private ProductAdapter adapter;
    private List<IProduct> productsList;
    private List<IProduct> allProducts;
    private List<IProduct> currentProductsShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Retrieve category
        Bundle extras = getIntent().getExtras();
        String category = extras.getString("key");

        vh = new ViewHolder();

        // Change header text and search hint
        if (Objects.equals(category, "Photos")) {
            vh.headerText.setText(R.string.Photos);
            vh.searchBar.setQueryHint("Search " + getString(R.string.Photos).toLowerCase());
        } else if (Objects.equals(category, "Paintings")) {
            vh.headerText.setText(R.string.Paintings);
            vh.searchBar.setQueryHint("Search " + getString(R.string.Paintings).toLowerCase());
        } else if (Objects.equals(category, "Digital")) {
            vh.headerText.setText(R.string.Digital);
            vh.searchBar.setQueryHint("Search " + getString(R.string.Digital).toLowerCase());
        } else {
            vh.headerText.setText("FAVOURITES");
            vh.searchBar.setQueryHint("Search Favourites");

        }

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
                    List<IProduct> toSort = new LinkedList<>(productsList);
                    productsList.clear();
                    productsList.addAll(FilterUtils.sortProductList(toSort, vh.priceSpinner.getSelectedItem().toString()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieving sort order (if set)
                String sort = vh.priceSpinner.getSelectedItem().toString().equals(getString(R.string.price_title)) ? "" : vh.priceSpinner.getSelectedItem().toString();

                // Retrieving filter set to filter on
                HashMap<String, String> filters = new HashMap<>();

                if (!vh.themeSpinner.getSelectedItem().toString().equals(getString(R.string.theme_title))) {
                    filters.put("theme", vh.themeSpinner.getSelectedItem().toString());
                }

                if (!vh.colourSpinner.getSelectedItem().toString().equals(getString(R.string.colour_title))) {
                    filters.put("mainColour", vh.colourSpinner.getSelectedItem().toString());
                }

                // Applies filters and sorting
                List<IProduct> filteredAndSorted = new LinkedList<>(
                        FilterUtils.applyFiltersAndSort(productsList, allProducts, filters, sort));
                productsList.clear();
                productsList.addAll(filteredAndSorted);
                currentProductsShowing = new LinkedList<>(productsList);
                adapter.notifyDataSetChanged();

                // If filter set is applied and no results, gives message
                if (!filters.isEmpty() && productsList.size()==0) {
                    Toast.makeText(getBaseContext(), "No products match the filter.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                vh.priceSpinner.setSelection(0);
                vh.colourSpinner.setSelection(0);
                vh.themeSpinner.setSelection(0);
            }
        };

        vh.themeSpinner.setOnItemSelectedListener(filterListener);
        vh.colourSpinner.setOnItemSelectedListener(filterListener);

        vh.backButton.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        });

        vh.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                vh.searchBar.clearFocus();
                productsList.clear();
                productsList.addAll(SearchUtils.getProductsBySearch(currentProductsShowing, s));
                adapter.notifyDataSetChanged();
                vh.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                productsList.clear();
                if (s.length() == 0) {
                    productsList.addAll(currentProductsShowing);
                } else {
                    productsList.addAll(SearchUtils.getProductsBySearch(currentProductsShowing, s));
                }

                // If filter set is applied and no results, gives message
                if (productsList.size()==0) {
                    Toast.makeText(getBaseContext(), "No products match search.", Toast.LENGTH_LONG).show();
                }

                adapter.notifyDataSetChanged();
                return false;
            }
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
                if (category.equals("Paintings")) {
                    productsList.addAll(task.getResult().toObjects(Painting.class));
                } else if (category.equals("Photos")) {
                    productsList.addAll(task.getResult().toObjects(Photo.class));
                } else if (category.equals("Digital")){
                    productsList.addAll(task.getResult().toObjects(Digital.class));
                } else {
                    productsList.addAll(task.getResult().toObjects(Product.class));
                }

                // Dynamically setting filters based off of available products in category
                FilterUtils.setFilterSpinnerHardcoded(this, vh.priceSpinner, R.array.price_filters);
                FilterUtils.setFilterSpinnerDynamic(this, vh.themeSpinner, productsList, "theme");
                FilterUtils.setFilterSpinnerDynamic(this, vh.colourSpinner, productsList, "mainColour");

                // Saving an instance of all the products for filtering
                allProducts.addAll(productsList);

                adapter.notifyDataSetChanged();

                vh.progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(getBaseContext(), "Loading products failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

}
