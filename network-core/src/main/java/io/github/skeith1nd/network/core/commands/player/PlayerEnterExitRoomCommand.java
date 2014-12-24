package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;

public class PlayerEnterExitRoomCommand extends Command {
    private boolean enter;
    private String roomId, playerJson;

    public PlayerEnterExitRoomCommand() {
        roomId = "";
        playerJson = "{}";
        enter = false;
        type = Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPlayerJson() {
        return playerJson;
    }

    public void setPlayerJson(String playerJson) {
        this.playerJson = playerJson;
    }
}
