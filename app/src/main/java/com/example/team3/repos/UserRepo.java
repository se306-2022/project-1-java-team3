package com.example.team3.repos;

import com.example.team3.models.product.IProduct;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class UserRepo implements IUserRepo{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public UserRepo() {}

    @Override
    public int getUserId() {
        return 0;
    }

    @Override
    public List<IProduct> getFavourites() {
        return null;
    }

    @Override
    public void addToFavourite(IProduct product) {

    }

    @Override
    public void removeFromFavourite(IProduct product) {

    }
}
