package com.example.team3.models.product;

import java.util.List;

public interface IProduct {
    int getId();
    String getName();
    String getArtist();
    List<String> getImages();
    int getPrice();
    String getMainColour();
    String getTheme();
    String getDetails();
    String getSize();
    int getViewCount();
}
