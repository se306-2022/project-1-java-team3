package com.example.team3.models.product;

import java.util.List;

public class Digital extends Product {
    public Digital(int id, String name, String artist, int year, List<String> images, int price,
                   String mainColour, String theme, String description, String size, int viewCount) {

        super(id, name, artist, year, images, price, mainColour, theme, description, size, viewCount);
    }

    public Digital() {}
}
