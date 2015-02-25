package io.github.skeith1nd.core.item;

import io.github.skeith1nd.core.game.AssetManager;
import io.github.skeith1nd.core.mouse.IMouseable;
import io.github.skeith1nd.core.world.Renderable;
import io.github.skeith1nd.core.world.World;
import io.github.skeith1nd.core.world.WorldObject;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;

public class WorldItem extends WorldObject implements Renderable, IMouseable {
    private GroupLayer layer;
    private ImageLayer imageLayer;
    private ItemInfo itemInfo;

    @Override
    public void init() {
        layer = PlayN.graphics().createGroupLayer();
        World.getInstance().getItemLayer().add(layer);

        // Image layer
        imageLayer = PlayN.graphics().createImageLayer(AssetManager.getInstance().getImages().get("images/items/" + itemInfo.getName() + ".png"));
        layer.add(imageLayer);

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

    public ImageLayer getImageLayer() {
        return imageLayer;
    }

    public void setImageLayer(ImageLayer imageLayer) {
        this.imageLayer = imageLayer;
    }

    @Override
    public void mouseLeftClick() {

    }

    @Override
    public void mouseRightClick() {

    }

    @Override
    public void mouseOver() {

    }

    @Override
    public void mouseOut() {

    }

    @Override
    public void mouseMove() {

    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }
}
