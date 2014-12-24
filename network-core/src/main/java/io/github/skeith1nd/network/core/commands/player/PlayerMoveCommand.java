package io.github.skeith1nd.network.core.commands.player;

import static playn.core.PlayN.*;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import playn.core.Json;

public class PlayerMoveCommand extends Command {
    public static final int MOVE_UP = 0x01;
    public static final int MOVE_LEFT = 0x02;
    public static final int MOVE_DOWN = 0x03;
    public static final int MOVE_RIGHT = 0x04;
    public static final int REST = 0x05;

    private Json.Object player;
    private int direction;
    private boolean validated;

    public PlayerMoveCommand() {
        reset();
    }

    public PlayerMoveCommand(Json.Object player, int direction) {
        this.type = Commands.PLAYER_MOVE_COMMAND;
        this.player = player;
        this.direction = direction;
        this.validated = false;
    }

    @Override
    public void reset() {
        type = Commands.PLAYER_MOVE_COMMAND;
        player = json().createObject();
        validated = false;
        direction = REST;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Json.Object serialize() {
        Json.Object result = json().createObject();
        result.put("player", player);
        result.put("type", type);
        result.put("direction", direction);
        result.put("validated", validated);

        return result;
    }

    @Override
    public void deserialize(Json.Object jsonObject) {
        reset();

        player = jsonObject.getObject("player");
        direction = (int)jsonObject.getInt("direction");
        validated = jsonObject.getBoolean("validated");
    }

    public Json.Object getPlayer() {
        return player;
    }

    public void setPlayer(Json.Object player) {
        this.player = player;
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