package com.example.team3.utils;

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
}
