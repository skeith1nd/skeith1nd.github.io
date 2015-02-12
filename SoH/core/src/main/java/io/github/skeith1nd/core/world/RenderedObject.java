package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.util.UpdateableTreeSet;

public abstract class RenderedObject extends WorldObject implements Renderable, UpdateableTreeSet.Updateable {
    @Override
    public void destroy() {
        if (Player.getInstance().getRoom() != null) {
            Room currentRoom = World.getInstance().getRooms().get(Player.getInstance().getRoom().getRoomId());
            currentRoom.getRenderedObjects().remove(this);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void update(Object o) {

    }
}
