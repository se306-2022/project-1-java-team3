package com.example.team3.data;

import com.example.team3.models.product.IProduct;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class DataProvider {
    private int[] paintingIds = new int[] {1,2,3,4,5};
    private int[] photoIds = new int [] {6,7,8,9,10};
    private int[] digitalIds = new int [] {11,12,13,14,15};

    public static void seedData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();


        // TODO: consume csv to populate database.
    }
}

