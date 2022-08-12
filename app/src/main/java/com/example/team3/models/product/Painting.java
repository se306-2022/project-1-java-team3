package com.example.team3.models.product;

import java.util.List;

public class Painting extends Product {
    public Painting(int id, String name, String artist, List<String> images, int price, String mainColour, String theme, String details, String size, int viewCount) {
        super(id, name, artist, images, price, mainColour, theme, details, size, viewCount);
    }

    public Painting() {}
}
