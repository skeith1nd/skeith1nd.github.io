package io.github.skeith1nd.core.ui;

import io.github.skeith1nd.core.world.Renderable;
import io.github.skeith1nd.core.world.World;
import playn.core.GroupLayer;
import playn.core.PlayN;

public abstract class UIRenderable implements Renderable {
    protected GroupLayer layer;
    protected int width, height;

    @Override
    public void init() {
        layer = PlayN.graphics().createGroupLayer();
        World.getInstance().getRenderableLayer().add(layer);
        updateLayer();
    }

    @Override
    public void updateLayer() {
        layer.setTranslation(width / 2, height);
    }

    @Override
    public void destroy() {
        World.getInstance().getRenderableLayer().remove(layer);
    }

    public GroupLayer getLayer() {
        return layer;
    }
}
