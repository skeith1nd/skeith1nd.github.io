package io.github.skeith1nd.core.world;

import playn.core.Image;

public interface Renderable {
    public void setImage(Image image);
    public Image getImage();
    public void destroy();
    public int getX();
    public int getY();
}
