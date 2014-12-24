package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;

public class PlayerLoginCommand extends Command {
    private String userId, playerJson, roomJson;
    private boolean success;

    public PlayerLoginCommand() {
        userId = "";
        playerJson = roomJson = "{}";
        success = false;
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

    public String getPlayerJson() {
        return playerJson;
    }

    public void setPlayerJson(String playerJson) {
        this.playerJson = playerJson;
    }

    public String getRoomJson() {
        return roomJson;
    }

    public void setRoomJson(String roomJson) {
        this.roomJson = roomJson;
    }
}
