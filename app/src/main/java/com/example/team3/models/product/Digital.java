package com.example.team3.models.product;

import java.util.List;

public class Digital extends Product {
    public Digital(int id, String name, String artist, int year, List<String> images, int price,
                   String mainColour, String theme, String description, int viewCount,
                   String category, boolean liked) {

        super(id, name, artist, year, images, price, mainColour, theme, description, viewCount, category, liked);
    }

    public Digital() {}
}
