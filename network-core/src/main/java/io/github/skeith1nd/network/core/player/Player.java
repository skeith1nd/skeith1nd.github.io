package io.github.skeith1nd.network.core.player;

import io.github.skeith1nd.network.core.INetworkObject;
import io.github.skeith1nd.network.core.room.Room;
import org.json.JSONObject;

public class Player implements INetworkObject {
    private String user, type, roomId;
    private int x, y;

    public Player() {
        reset();
    }

    public void reset() {
        user = type = roomId = "";
        x = y = 0;
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("user", user);
        result.put("type", type);
        result.put("x", x);
        result.put("y", y);
        result.put("room", roomId);
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
        roomId = jsonObject.getString("room");
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
