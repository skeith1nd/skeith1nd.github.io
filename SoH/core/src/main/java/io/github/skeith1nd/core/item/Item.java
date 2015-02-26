package io.github.skeith1nd.core.item;

import playn.core.Image;

public class Item {
    public static final int DAGGER = 0;
    public static final int SWORD = 1;
    public static final int BOW = 2;

    private Image image;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
