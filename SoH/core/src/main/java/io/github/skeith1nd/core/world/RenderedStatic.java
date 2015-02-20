package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.mouse.MouseAdapter;
import playn.core.ImageLayer;
import playn.core.PlayN;

public class RenderedStatic extends RenderedObject {
    private int tileGid, tilesWide, tilesTall;
    private ImageLayer imageLayer;

    public void init(int tilesPerRow, int tileWidth, int tileHeight) {
        super.init();

        int tileRow = tileGid / tilesPerRow;
        int tileCol = tileGid % tilesPerRow;

        int imageX = tileCol * tileWidth;
        int imageY = tileRow * tileHeight;

        width = tilesWide * tileWidth;
        height = tilesTall * tileHeight;

        imageLayer = PlayN.graphics().createImageLayer(World.getInstance().getTerrainTileSheet().subImage(imageX, imageY, width, height));
        imageLayer.addListener(new MouseAdapter(this));
        layer.add(imageLayer);
        updateLayer();
    }

    @Override
    public void mouseLeftClick() {
        System.out.println("left click static object");
    }

    @Override
    public void mouseRightClick() {
        System.out.println("right click static object");
    }

    @Override
    public void mouseOver() {
        System.out.println("mouse over static object");
    }

    @Override
    public void mouseOut() {
        System.out.println("mouse out static object");
    }

    @Override
    public void mouseMove() {
        System.out.println("mouse move static object");
    }

    public int getTileGid() {
        return tileGid;
    }

    public void setTileGid(int tileGid) {
        this.tileGid = tileGid;
    }

    public int getTilesWide() {
        return tilesWide;
    }

    public void setTilesWide(int tilesWide) {
        this.tilesWide = tilesWide;
    }

    public int getTilesTall() {
        return tilesTall;
    }

    public void setTilesTall(int tilesTall) {
        this.tilesTall = tilesTall;
    }
}
