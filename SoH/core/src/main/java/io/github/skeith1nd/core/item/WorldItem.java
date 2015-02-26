package io.github.skeith1nd.core.item;

import io.github.skeith1nd.core.mouse.IMouseable;
import io.github.skeith1nd.core.mouse.MouseAdapter;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.core.ui.Inventory;
import io.github.skeith1nd.core.util.MathUtil;
import io.github.skeith1nd.core.world.Renderable;
import io.github.skeith1nd.core.world.World;
import io.github.skeith1nd.core.world.WorldObject;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;

public class WorldItem extends WorldObject implements Renderable, IMouseable {
    private GroupLayer layer;
    private ImageLayer imageLayer, hoverImageLayer;
    private CanvasImage hoverImage;
    private Item item;

    public WorldItem(int item) {
        this.item = ItemManager.getInstance().getItem(item);
        width = (int)this.item.getImage().width();
        height = (int)this.item.getImage().height();

        hoverImage = PlayN.graphics().createImage(width, height);
        hoverImageLayer = PlayN.graphics().createImageLayer(hoverImage);
        hoverImageLayer.setDepth(0);
    }

    @Override
    public void init() {
        layer = PlayN.graphics().createGroupLayer();
        World.getInstance().getItemLayer().add(layer);

        // Image layer
        imageLayer = PlayN.graphics().createImageLayer(item.getImage());
        imageLayer.setDepth(1);
        layer.add(imageLayer);
        updateLayer();

        // Mouse adapter
        imageLayer.addListener(new MouseAdapter(this));
    }

    @Override
    public void updateLayer() {
        // Render hover imagae
        renderHoverImage();

        // Update layer position and depth
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
        int distance = MathUtil.distance(Player.getInstance().getX(), Player.getInstance().getY(), x, y);
        if (distance < 50) {
            Inventory.getInstance().add(this);
        }
    }

    @Override
    public void mouseRightClick() {

    }

    @Override
    public void mouseOver() {
        layer.add(hoverImageLayer);
    }

    @Override
    public void mouseOut() {
        layer.remove(hoverImageLayer);
    }

    @Override
    public void mouseMove() {

    }

    private void renderHoverImage() {
        hoverImage.canvas().setStrokeColor(0xFFFFFFFF);
        hoverImage.canvas().setStrokeWidth(1f);
        hoverImage.canvas().strokeRect(0, 0, width - 1, height - 1);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
