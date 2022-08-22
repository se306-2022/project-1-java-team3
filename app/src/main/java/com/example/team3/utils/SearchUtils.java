package com.example.team3.utils;

import com.example.team3.models.product.IProduct;

import java.util.LinkedList;
import java.util.List;

public class SearchUtils {

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
