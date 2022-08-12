package com.example.team3.models.user;

import java.util.List;

public interface IUser {
    int getId();
    List<Integer> getFavourites();
    void addToFavourites(int id);
    void removeFromFavourites(int id);
}
