package io.github.skeith1nd.game;

import io.github.skeith1nd.data.User;

import java.util.HashMap;

public class ServerRoom {
    private String roomId;
    private HashMap<String, User> users;

    public ServerRoom(String roomId) {
        this.roomId = roomId;
        users = new HashMap<String, User>();
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
