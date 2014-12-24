package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;

public class PlayerMoveCommand extends Command {
    public static final int MOVE_UP = 0x01;
    public static final int MOVE_LEFT = 0x02;
    public static final int MOVE_DOWN = 0x03;
    public static final int MOVE_RIGHT = 0x04;
    public static final int REST = 0x05;

    private String playerJson;
    private int direction;
    private boolean validated;

    public PlayerMoveCommand() {
        playerJson = "{}";
        direction = REST;
        validated = false;
        type = Commands.PLAYER_MOVE_COMMAND;
    }

    public String getPlayerJson() {
        return playerJson;
    }

    public void setPlayerJson(String playerJson) {
        this.playerJson = playerJson;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}