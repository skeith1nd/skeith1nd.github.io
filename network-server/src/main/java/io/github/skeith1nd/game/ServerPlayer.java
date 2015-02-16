package io.github.skeith1nd.game;

import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class ServerPlayer {
    private int x, y, currentHP, maxHP;
    private String type, roomId, userId;
    private WebSocket webSocket;

    public void fromJSON(JSONObject jsonObject) {
        x = jsonObject.getInt("x");
        y = jsonObject.getInt("y");
        type = jsonObject.getString("type");
        roomId = jsonObject.getString("roomId");
        userId = jsonObject.getString("userId");
        currentHP = jsonObject.getInt("currentHP");
        maxHP = jsonObject.getInt("maxHP");
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("type", type);
        jsonObject.put("roomId", roomId);
        jsonObject.put("userId", userId);
        jsonObject.put("currentHP", currentHP);
        jsonObject.put("maxHP", maxHP);
        return jsonObject;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
