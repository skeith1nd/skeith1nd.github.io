package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.Player;
import playn.core.Surface;

public class World {
    private Player player;

    public World() {
        player = new Player();
    }

    public void paint(Surface surface) {
        if (player.isLoaded()) {
            surface.drawImage(player.getCurrentImage(), player.getX(), player.getY());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
