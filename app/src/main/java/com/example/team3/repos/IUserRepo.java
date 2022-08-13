package com.example.team3.repos;

import com.example.team3.models.product.IProduct;

import java.util.List;

public interface IUserRepo {
    int getUserId();
    List<IProduct> getFavourites();
    void addToFavourite(IProduct product);
    void removeFromFavourite(IProduct product);
}
