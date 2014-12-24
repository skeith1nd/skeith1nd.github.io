package io.github.skeith1nd.network.core.commands.player;

import static playn.core.PlayN.*;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import playn.core.Json;

public class PlayerLoginCommand extends Command {
    private String userId;
    private boolean success;
    private Json.Object player, room;

    public PlayerLoginCommand() {
        reset();
    }

    public PlayerLoginCommand(String userId) {
        this.success = false;
        this.player = json().createObject();
        this.room = json().createObject();
        this.userId = userId;
    }

    @Override
    public Json.Object serialize() {
        Json.Object result = json().createObject();
        result.put("userId", userId);
        result.put("type", type);
        result.put("success", success);
        result.put("player", player);
        result.put("room", room);

        return result;
    }

    @Override
    public void deserialize(Json.Object jsonObject) {
        reset();

        userId = jsonObject.getString("userId");
        success = jsonObject.getBoolean("success");
        player = jsonObject.getObject("player");
        room = jsonObject.getObject("room");
    }

    @Override
    public void reset() {
        userId = "";
        success = false;
        player = json().createObject();
        room = json().createObject();
        type = Commands.PLAYER_LOGIN_COMMAND;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Json.Object getPlayer() {
        return player;
    }

    public void setPlayer(Json.Object player) {
        this.player = player;
    }

    public Json.Object getRoom() {
        return room;
    }

    public void setRoom(Json.Object room) {
        this.room = room;
    }
}
