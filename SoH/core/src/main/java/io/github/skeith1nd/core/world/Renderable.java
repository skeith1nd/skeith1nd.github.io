package io.github.skeith1nd.core.world;


import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Surface;

public interface Renderable {
    public GroupLayer getLayer();
    public void init();
    public void updateLayer();
    public void destroy();
    public int getX();
    public int getY();
}
