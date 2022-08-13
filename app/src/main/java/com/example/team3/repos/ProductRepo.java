package com.example.team3.repos;

import com.example.team3.models.product.IProduct;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class ProductRepo implements IProductRepo {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public ProductRepo() {}

    @Override
    public List<IProduct> getAllProducts() {
        return null;
    }

    @Override
    public List<IProduct> getPaintings() {
        return null;
    }

    @Override
    public List<IProduct> getDigital() {
        return null;
    }

    @Override
    public List<IProduct> getPhotos() {
        return null;
    }

    @Override
    public IProduct getProductById(int id) {
        return null;
    }

    @Override
    public List<IProduct> getFeaturedProducts(int id) {
        return null;
    }

    @Override
    public List<IProduct> getProductsByFilter(int minPrice, int maxPrice, String theme, String colour) {
        return null;
    }

    @Override
    public List<IProduct> getProductsBySearch(String query) {
        return null;
    }
}
