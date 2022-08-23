package com.example.team3.models.product;

import java.util.List;

public class Photo extends Product {

    private String camera;

    @Override
    public String getCamera() {
        return camera;
    }

    public Photo(int id, String name, String artist, int year, List<String> images, int price,
                 String mainColour, String theme, String description, int viewCount,
                 String category, boolean liked, String camera) {
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
        this.camera = camera;
    }

    public Photo() {}
}
