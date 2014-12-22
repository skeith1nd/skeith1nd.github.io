package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.room.Room;
import playn.core.Surface;

import java.util.HashMap;

public class World {
    private static World instance;
    private HashMap<String, Room> rooms;

    private World() {
        rooms = new HashMap<String, Room>();
        initializeRooms();
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
            for (io.github.skeith1nd.network.core.player.Player player : Player.getInstance().getRoom().getPlayers().values()) {
                surface.drawImage()
            }
        }
    }

    private void initializeRooms() {
        Room room1 = new Room();
        room1.setRoomId("1");
        rooms.put("1", room1);
    }
}
