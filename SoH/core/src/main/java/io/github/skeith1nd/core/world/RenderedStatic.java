package io.github.skeith1nd.core.world;

import playn.core.Image;

public class RenderedStatic extends RenderedObject {
    private int tileGid, tilesWide, tilesTall;
    private Image image;

    public void init(int tilesPerRow, int tileWidth, int tileHeight) {
        int tileRow = tileGid / tilesPerRow;
        int tileCol = tileGid % tilesPerRow;

        int imageX = tileCol * tileWidth;
        int imageY = tileRow * tileHeight;

        width = tilesWide * tileWidth;
        height = tilesTall * tileHeight;

        image = World.getInstance().getTerrainTileSheet().subImage(imageX, imageY, width, height);
    }

    @Override
    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public Image getImage() {
        return image;
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
