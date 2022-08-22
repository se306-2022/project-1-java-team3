package com.example.team3.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.team3.R;
import com.example.team3.models.product.IProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FilterUtils {

    /**
     * Retrieves product list of products that have been filtered by a filter set and then sorted
     *
     * @param  currentProductsShowing  current products shown on list activity
     * @param  allProducts  products to apply filter set to
     * @param  filters  hashmap of <filterType, filterValue>
     * @param  sortDirection "Low to High" or "High to Low"
     */
    public static List <IProduct> applyFiltersAndSort (List<IProduct> currentProductsShowing, List<IProduct> allProducts, HashMap<String, String> filters, String sortDirection) {
        // If filters have been set and then reset, will repopulate products list with all products in category
        List <IProduct> filteredProducts = new LinkedList<>();

        // Apply filtering to all products to get filtered list
        if (filters.isEmpty() && (!currentProductsShowing.equals(allProducts))) {
            filteredProducts.addAll(allProducts);

        } else if (!filters.isEmpty()) {
            filteredProducts.addAll(filterProductList(allProducts, filters)); // Will filter based on theme and colour
        }

        // Apply sort to filtered list
        if (sortDirection.length()>0) {
            List<IProduct> toSort = new LinkedList<>(filteredProducts);
            filteredProducts.clear();
            filteredProducts.addAll(FilterUtils.sortProductList(toSort, sortDirection));
        }

        return filteredProducts;
    }

    /**
     * Retrieves product list of products that have been filtered by a filter set
     *
     * @param  listToFilter  list of products to apply filter on
     * @param  filters  hashmap of <filterType, filterValue>
     * @return List<IProduct> list of products that match filter set
     */
    public static List<IProduct> filterProductList(List<IProduct> listToFilter, HashMap<String,String> filters) {
        List<IProduct> filteredProducts = new LinkedList<>();

        boolean multipleFilters = (filters.size() > 1);

        // If multiple filters, will combine filters (filter a && filter b)
        if (multipleFilters) {
            List<IProduct> tempFilteredProducts = filterByTheme(listToFilter, filters.get("theme"));
            filteredProducts = filterByColour(tempFilteredProducts, filters.get("mainColour"));
        } else if (((String)filters.keySet().toArray()[0]).equals("theme")){
            filteredProducts = filterByTheme(listToFilter, filters.get("theme"));
        } else if (((String)filters.keySet().toArray()[0]).equals("mainColour")){
            filteredProducts = filterByColour(listToFilter, filters.get("mainColour"));
        }

        return filteredProducts;

    }

    /**
     * Filters list of products by theme
     *
     * @param  listToFilter  list of products to apply filter on
     * @param  filterValue  value to filter on
     * @return List<IProduct> filtered list
     */
    private static List<IProduct> filterByTheme(List<IProduct> listToFilter, String filterValue) {
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
     * @param  listToFilter  list of products to apply filter on
     * @param  filterValue  value to filter on
     * @return List<IProduct> filtered list
     */
    private static List<IProduct> filterByColour(List<IProduct> listToFilter, String filterValue) {
        filterValue = filterValue.toLowerCase();
        List<IProduct> filteredList = new LinkedList<>();
        for (IProduct product : listToFilter) {
            if (product.getMainColour().equals(filterValue)) {
                filteredList.add(product);
            }
        }

        return filteredList;
    }

    /**
     * Sorts list of products by ascending or descending order
     *
     * @param  listToSort  list of products to apply filter on>
     * @param  direction  "Low to High" or "High to Low"
     * @return List<IProduct> sorted list
     */
    public static List<IProduct> sortProductList(List<IProduct> listToSort, String direction) {
        Collections.sort(listToSort,
                (p1, p2) -> p1.getPrice() - p2.getPrice());

        if (direction.equals("High to Low")){
            Collections.reverse(listToSort);
        }

        return listToSort;
    }

    /**
     * Sets filter spinner drop down values to set values
     *
     * @param  c  current context
     * @param  spinner  spinner to set dropdown options for
     * @param  options static list of options to populate - resource file
     */
    public static void setFilterSpinnerHardcoded(Context c, Spinner spinner, int options) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(c,
                options, R.layout.filter_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    /**
     * Sets filter spinner drop down values dynamically based on the available products
     *
     * @param  c  current context
     * @param  spinner  spinner to set dropdown options for
     * @param  productsList List of products currently loaded for category
     * @param  type filter type - implemented for theme and colour
     */
    public static void setFilterSpinnerDynamic(Context c, Spinner spinner, List<IProduct> productsList, String type) {
        Set<String> set = new HashSet<>();
        String currentProduct="";

        for (IProduct product : productsList) {
            if (type.equals("theme")) {
                currentProduct = product.getTheme();
            } else if (type.equals("mainColour")) {
                currentProduct = product.getMainColour();
            }
            set.add(currentProduct.substring(0, 1).toUpperCase() + currentProduct.substring(1));
        }

        ArrayList<String> options = new ArrayList<>(set);
        if (type.equals("theme")) {
            options.add(0, c.getString(R.string.theme_title));
        } else if (type.equals("mainColour")) {
            options.add(0, c.getString(R.string.colour_title));
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<> (c, R.layout.filter_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}
