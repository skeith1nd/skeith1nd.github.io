package io.github.skeith1nd.network.core.player;

import io.github.skeith1nd.network.core.INetworkObject;
import io.github.skeith1nd.network.core.room.Room;
import org.json.JSONObject;

public class Player implements INetworkObject {
    private String user, type;
    private int x, y;
    private Room room;

    public Player() {
        reset();
    }

    public void reset() {
        user = "";
        x = y = 0;
        type = "";
        room = new Room();
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("user", user);
        result.put("type", type);
        result.put("x", x);
        result.put("y", y);
        result.put("room", room.serialize());
        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        reset();

        user = jsonObject.getString("user");
        type = jsonObject.getString("type");
        x = jsonObject.getInt("x");
        y = jsonObject.getInt("y");
        room.deserialize(jsonObject.getJSONObject("room").toString());
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
