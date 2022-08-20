package com.example.team3.repos;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.team3.models.product.IProduct;
import com.example.team3.models.product.Photo;
import com.example.team3.models.product.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
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
        List<IProduct> photoList = new LinkedList<IProduct>();

        db.collection("numbers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot results = task.getResult();
                for (IProduct product : task.getResult().toObjects(Photo.class)) {
                    photoList.add(product);
                    Log.i("danikadebug", product.getName() + " loaded.");
                }
                if (photoList.size() > 0) {
                    Log.i("Getting products", "Success");

                } else
                    Log.e("danikadebug", "getPhotos: collection empty");

            } else
                Log.e("danikadebug", "getPhotos: firebase empty");
        });

        Log.d("danikadebug", photoList.toString());
        return photoList;
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
