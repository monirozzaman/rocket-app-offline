package com.itvillage.dev.offlinebkashap.model;

/**
 * Created by monirozzamanroni on 7/18/2019.
 */

public class ScreenItems {
    String title, description;
    int image;

    public ScreenItems(String title, String description, int image) {

        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }
}
