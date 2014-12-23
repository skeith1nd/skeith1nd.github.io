package io.github.skeith1nd.game;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import org.java_websocket.WebSocket;

public class ServerPlayer {
    private int x, y;
    private String type, roomId, userId;
    private WebSocket webSocket;

    public void fromJSON(JSONObject jsonObject) {
        x = (int)jsonObject.get("x").isNumber().doubleValue();
        y = (int)jsonObject.get("y").isNumber().doubleValue();
        type = jsonObject.get("type").isString().stringValue();
        roomId = jsonObject.get("roomId").isString().stringValue();
        userId = jsonObject.get("userId").isString().stringValue();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", new JSONNumber(x));
        jsonObject.put("y", new JSONNumber(y));
        jsonObject.put("type", new JSONString(type));
        jsonObject.put("roomId", new JSONString(roomId));
        jsonObject.put("userId", new JSONString(userId));
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
