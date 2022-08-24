package com.example.team3.utils;

import android.content.Context;

import com.example.team3.data.DataProvider;
import com.example.team3.models.product.IProduct;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUtils {

    public static CollectionReference getCollectionReference(String type) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        switch(type) {
            case "painting":
                return db.collection("Paintings");
            case "digital":
                return db.collection("Digital");
            case "photo":
                return db.collection("Photos");
            default:
                return null;
        }
    }

    /**
     * Helper method to add product to favourites collection.
     * @param product IProduct model.
     */
    public static void addProductToFavourites(IProduct product) {
        CollectionReference ref = getCollectionReference(product.getCategory());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ref.document(String.valueOf(product.getId())).update("liked", true)
                .addOnSuccessListener(unused ->
                        db.collection("Favourites")
                                .document(String.valueOf(product.getId())).set(product)
                                .addOnFailureListener(Throwable::printStackTrace)
                );
    }

    /**
     * Helper method to remove product from favourites collection.
     * @param product IProduct model.
     */
    public static void removeProductFromFavourites(IProduct product) {
        CollectionReference ref = FirestoreUtils.getCollectionReference(product.getCategory());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ref.document(String.valueOf(product.getId())).update("liked", false)
                .addOnSuccessListener(unused ->
                        db.collection("Favourites")
                                .document(String.valueOf(product.getId())).delete()
                                .addOnFailureListener(Throwable::printStackTrace)
                );
    }

    /**
     * Helper method to seed data to firebase.
     */
    public static void seedData(Context context) {
        DataProvider dp = new DataProvider(context);
        dp.seedData();
    }
}
