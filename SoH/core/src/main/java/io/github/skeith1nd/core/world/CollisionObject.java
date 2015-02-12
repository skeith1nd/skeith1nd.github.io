package io.github.skeith1nd.core.world;

public class CollisionObject extends WorldObject implements Collidable {
    private int collisionWidth, collisionHeight;

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
