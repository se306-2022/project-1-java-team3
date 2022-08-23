package com.example.team3.models.product;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Product implements IProduct {

    protected int id, year, price, viewCount;
    protected String name, artist, mainColour, theme, description, category;
    protected List<String> images;
    protected boolean liked;

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public List<String> getImages() {
        return images;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getMainColour() {
        return mainColour;
    }

    @Override
    public String getTheme() {
        return theme;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getViewCount() {
        return viewCount;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public boolean getLiked() {
        return liked;
    }

    @Override
    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    // Category specific methods:

    @Exclude
    public String getMedium() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getCamera() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getBlockchain() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Exclude
    public String getTokenId() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }
}
