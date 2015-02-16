package io.github.skeith1nd.core.world;


import playn.core.Image;
import playn.core.Surface;

public interface Renderable {
    public void setImage(Image image);
    public Image getImage();
    public void render(Surface surface);
    public void destroy();
    public int getX();
    public int getY();
}
