package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.util.UpdateableTreeSet;
import playn.core.Image;

public abstract class Renderable implements UpdateableTreeSet.Updateable{
    protected int x, y, width, height, collisionWidth, collisionHeight;
    protected Image image;

    public Image getImage() {
        return image;
    }

    public void destroy() {
        if (Player.getInstance().getRoom() != null) {
            Room currentRoom = World.getInstance().getRooms().get(Player.getInstance().getRoom().getRoomId());
            currentRoom.getObjects().remove(this);
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCollisionWidth() {
        return collisionWidth;
    }

    public void setCollisionWidth(int collisionWidth) {
        this.collisionWidth = collisionWidth;
    }

    public int getCollisionHeight() {
        return collisionHeight;
    }

    public void setCollisionHeight(int collisionHeight) {
        this.collisionHeight = collisionHeight;
    }
}
