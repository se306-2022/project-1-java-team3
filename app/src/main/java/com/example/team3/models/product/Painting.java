package com.example.team3.models.product;

import java.util.List;

public class Painting extends Product {

    private String medium;

    @Override
    public String getMedium() {
        return medium;
    }

    public Painting(int id, String name, String artist, int year, List<String> images, int price,
                    String mainColour, String theme, String description, int viewCount,
                    String category, boolean liked, String medium) {
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
        this.medium = medium;
    }

    public Painting() {}
}
