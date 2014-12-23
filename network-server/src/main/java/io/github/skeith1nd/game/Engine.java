package io.github.skeith1nd.game;

import java.util.HashMap;

public class Engine {
    private static Engine instance;
    private HashMap<String, ServerRoom> rooms = new HashMap<String, ServerRoom>();

    private Engine() {}
    public static Engine getInstance() {
        if (instance == null) instance = new Engine();
        return instance;
    }

    public void init() {
        ServerRoom serverRoom = new ServerRoom("1");
        rooms.put(serverRoom.getRoomId(), serverRoom);
    }

    public HashMap<String, ServerRoom> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, ServerRoom> rooms) {
        this.rooms = rooms;
    }
}
