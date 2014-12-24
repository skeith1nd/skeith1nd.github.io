package io.github.skeith1nd.game;

import org.java_websocket.WebSocket;
import playn.core.Json;
import playn.core.PlayN;

public class ServerPlayer {
    private int x, y;
    private String type, roomId, userId;
    private WebSocket webSocket;

    public void fromJSON(Json.Object jsonObject) {
        x = jsonObject.getInt("x");
        y = jsonObject.getInt("y");
        type = jsonObject.getString("type");
        roomId = jsonObject.getString("roomId");
        userId = jsonObject.getString("userId");
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = PlayN.json().createObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("type", type);
        jsonObject.put("roomId", roomId);
        jsonObject.put("userId", userId);
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
}
