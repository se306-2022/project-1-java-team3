package com.example.team3.models.product;

import java.util.List;

public class Digital extends Product {

    private String tokenId;
    private String blockchain;

    @Override
    public String getBlockchain() {
        return blockchain;
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    public Digital(int id, String name, String artist, int year, List<String> images, int price,
                   String mainColour, String theme, String description, int viewCount,
                   String category, boolean liked, String blockchain, String tokenId) {
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
        this.blockchain = blockchain;
        this.tokenId = tokenId;
    }

    public Digital() {}
}
