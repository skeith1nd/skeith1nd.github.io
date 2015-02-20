package io.github.skeith1nd.core.player;

import playn.core.*;

public class ClientPlayer extends PlayerEntity {
    private String roomId;
    public ClientPlayer() {}

    public void fromJSON(Json.Object jsonObject) {
        super.fromJSON(jsonObject);
        roomId = jsonObject.getString("roomId");
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = super.toJSON();
        jsonObject.put("roomId", roomId);
        return jsonObject;
    }

    public void init() {
        super.init();
    }

    public void moveUp(){
        super.moveUp();
        y -= 3;
        updateLayer();
    }

    public void moveLeft(){
        super.moveLeft();
        x -= 3;
        updateLayer();
    }

    public void moveDown(){
        super.moveDown();
        y += 3;
        updateLayer();
    }

    public void moveRight(){
        super.moveRight();
        x += 3;
        updateLayer();
    }

    public void setServerPosition(int serverX, int serverY) {
        if (x != serverX) x = serverX;
        if (y != serverY) y = serverY;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
