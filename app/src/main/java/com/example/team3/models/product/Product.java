package com.example.team3.models.product;

import java.util.List;

public class Product implements IProduct {

    private int id;
    private int year;
    private String name;
    private String artist;
    private List<String> images;
    private int price;
    private String mainColour;
    private String theme;
    private String description;
    private int viewCount;
    private String category;
    private boolean liked;

    public Product(int id, String name, String artist, int year, List<String> images, int price,
                   String mainColour, String theme, String description, int viewCount,
                   String category, boolean liked) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.images = images;
        this.price = price;
        this.mainColour = mainColour;
        this.theme = theme;
        this.description = description;
        this.viewCount = viewCount;
        this.year = year;
        this.category = category;
        this.liked = liked;
    }

    public Product() {
    }

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
}
