package com.example.team3.utils;

import com.example.team3.models.product.IProduct;

import java.util.LinkedList;
import java.util.List;

public class SearchUtils {

    /**
     * Helper method returns altered product list according to search query.
     *
     * @param productsToSearch list of IProducts to search.
     * @param query string query pattern.
     * @return a new list of filtered products.
     */
    public static List<IProduct> getProductsBySearch(List<IProduct> productsToSearch, String query) {
        List<IProduct> productsList = new LinkedList<>();
        query = query.trim().toLowerCase();
        for (IProduct product : productsToSearch) {
            if (product.getName().toLowerCase().contains(query)) {
                productsList.add(product);
            }
        }
        return productsList;
    }
}
