package com.example.team3.models.product;

import java.util.List;

public interface IProduct {
    // Applies to all categories.
    int getId();
    String getName();
    int getYear();
    String getArtist();
    List<String> getImages();
    int getPrice();
    String getMainColour();
    String getTheme();
    String getDescription();
    int getViewCount();
    String getCategory();
    boolean getLiked();

    void setLiked(boolean liked);

    // Category specific properties:
    // Painting
    String getMedium();

    // Photograph
    String getCamera();

    // Digital
    String getBlockchain();
    String getTokenId();
}
