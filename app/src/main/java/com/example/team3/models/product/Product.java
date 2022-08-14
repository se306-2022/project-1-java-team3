package com.example.team3.models.product;

import java.util.List;

public abstract class Product implements IProduct {

    private int id;
    private int year;
    private String name;
    private String artist;
    private List<String> images;
    private int price;
    private String mainColour;
    private String theme;
    private String description;
    private String size;
    private int viewCount;

    public Product(int id, String name, String artist, int year, List<String> images, int price,
                   String mainColour, String theme, String description, String size, int viewCount) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.images = images;
        this.price = price;
        this.mainColour = mainColour;
        this.theme = theme;
        this.description = description;
        this.size = size;
        this.viewCount = viewCount;
        this.year = year;
    }

    public Product() {}

    @Override
    public int getYear() {return year;}

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
    public String getSize() {
        return size;
    }

    @Override
    public int getViewCount() {
        return viewCount;
    }
}
