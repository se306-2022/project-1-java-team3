package com.example.team3.repos;

import com.example.team3.models.product.IProduct;

import java.util.List;

public interface IProductRepo {
    List<IProduct> getAllProducts();
    List<IProduct> getPaintings();
    List<IProduct> getDigital();
    List<IProduct> getPhotos();

    IProduct getProductById(int id);
    List<IProduct> getFeaturedProducts(int id);
    List<IProduct> getProductsByFilter(int minPrice, int maxPrice, String theme, String colour);
    List<IProduct> getProductsBySearch(String query);
}
