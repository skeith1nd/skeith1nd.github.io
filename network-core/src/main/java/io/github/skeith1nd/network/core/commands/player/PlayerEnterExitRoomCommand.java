package io.github.skeith1nd.network.core.commands.player;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;

public class PlayerEnterExitRoomCommand extends Command {
    private boolean enter;
    private JSONObject player;
    private String roomId;

    public PlayerEnterExitRoomCommand() {
        reset();
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("type", new JSONNumber(type));
        result.put("enter", JSONBoolean.getInstance(enter));
        result.put("roomId", new JSONString(roomId));
        result.put("player", player);

        return result;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        reset();

        enter = jsonObject.get("enter").isBoolean().booleanValue();
        roomId = jsonObject.get("roomId").isString().stringValue();
        player = jsonObject.get("player").isObject();
    }

    @Override
    public void reset() {
        type = Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND;
        enter = true;
        player = new JSONObject();
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public JSONObject getPlayer() {
        return player;
    }

    public void setPlayer(JSONObject player) {
        this.player = player;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
