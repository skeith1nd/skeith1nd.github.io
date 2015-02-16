package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.util.UpdateableTreeSet;
import playn.core.Surface;

public abstract class RenderedObject extends WorldObject implements Renderable, UpdateableTreeSet.Updateable {
    @Override
    public void destroy() {
        if (Player.getInstance().getRoom() != null) {
            Room currentRoom = World.getInstance().getRooms().get(Player.getInstance().getRoom().getRoomId());
            currentRoom.getRenderedObjects().remove(this);
        }
    }

    @Override
    public void render(Surface surface) {
        surface.drawImage(getImage(),
                getX() - getWidth() / 2,
                getY() - getHeight());
    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void update() {

    }
}
