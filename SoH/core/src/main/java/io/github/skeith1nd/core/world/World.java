package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import playn.core.Surface;

public class World {
    private static World instance;

    private World() {
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    public void paint(Surface surface) {
        if (Player.getInstance().isLoaded()) {
            surface.drawImage(Player.getInstance().getCurrentImage(), Player.getInstance().getX(), Player.getInstance().getY());

            // Draw other players in the room
            for (ClientPlayer player : Player.getInstance().getRoom().getPlayers().values()) {
                if (!player.getUserId().equals(Player.getInstance().getUserId()) && player.isLoaded())
                    surface.drawImage(player.getCurrentImage(), player.getX(), player.getY());
            }
        }
    }
}
