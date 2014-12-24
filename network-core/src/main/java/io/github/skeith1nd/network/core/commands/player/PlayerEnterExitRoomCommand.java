package io.github.skeith1nd.network.core.commands.player;

import static playn.core.PlayN.*;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import playn.core.Json;

public class PlayerEnterExitRoomCommand extends Command {
    private boolean enter;
    private Json.Object player;
    private String roomId;

    public PlayerEnterExitRoomCommand() {
        reset();
    }

    @Override
    public Json.Object serialize() {
        Json.Object result = json().createObject();
        result.put("type", type);
        result.put("enter", enter);
        result.put("roomId", roomId);
        result.put("player", player);
        return result;
    }

    @Override
    public void deserialize(Json.Object jsonObject) {
        reset();

        enter = jsonObject.getBoolean("enter");
        roomId = jsonObject.getString("roomId");
        player = jsonObject.getObject("player");
    }

    @Override
    public void reset() {
        type = Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND;
        enter = true;
        player = json().createObject();
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public Json.Object getPlayer() {
        return player;
    }

    public void setPlayer(Json.Object player) {
        this.player = player;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
