package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.mouse.IMouseable;
import playn.core.GroupLayer;
import playn.core.PlayN;

public abstract class RenderedObject extends WorldObject implements Renderable, IMouseable {
    protected GroupLayer layer;

    @Override
    public void init() {
        layer = PlayN.graphics().createGroupLayer();
        World.getInstance().getRenderableLayer().add(layer);
        updateLayer();
    }

    @Override
    public void updateLayer() {
        layer.setTranslation(x - width / 2, y - height);
        layer.setDepth(y);
    }

    @Override
    public void destroy() {
        World.getInstance().getRenderableLayer().remove(layer);
    }

    public GroupLayer getLayer() {
        return layer;
    }
}
