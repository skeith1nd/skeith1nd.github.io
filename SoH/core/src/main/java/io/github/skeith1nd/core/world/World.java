package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.Player;
import playn.core.Surface;

public class World {
    private static World instance;

    private World() {}
    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    public void paint(Surface surface) {
        if (Player.getInstance().isLoaded()) {
            surface.drawImage(Player.getInstance().getCurrentImage(), Player.getInstance().getX(), Player.getInstance().getY());
        }
    }
}
