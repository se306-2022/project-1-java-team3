package com.example.team3.models.user;

import java.util.List;

public class User implements IUser {
    private int id;
    private List<Integer> favourites;

    public User(int id, List<Integer> favourites) {
        this.id = id;
        this.favourites = favourites;
    }

    public User() {}

    @Override
    public int getId() {
       return id;
    }

    @Override
    public List<Integer> getFavourites() {
        return favourites;
    }

    @Override
    public void addToFavourites(int id) {
        favourites.add(id);
    }

    @Override
    public void removeFromFavourites(int id) {
        favourites.remove(Integer.valueOf(id));
    }
}
