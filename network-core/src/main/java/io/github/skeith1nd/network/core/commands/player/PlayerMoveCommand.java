package io.github.skeith1nd.network.core.commands.player;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;

public class PlayerMoveCommand extends Command {
    public static final int MOVE_UP = 0x01;
    public static final int MOVE_LEFT = 0x02;
    public static final int MOVE_DOWN = 0x03;
    public static final int MOVE_RIGHT = 0x04;
    public static final int REST = 0x05;

    private JSONObject player;
    private int direction;
    private boolean validated;

    public PlayerMoveCommand() {
        reset();
    }

    public PlayerMoveCommand(JSONObject player, int direction) {
        this.type = Commands.PLAYER_MOVE_COMMAND;
        this.player = player;
        this.direction = direction;
        this.validated = false;
    }

    @Override
    public void reset() {
        type = Commands.PLAYER_MOVE_COMMAND;
        player = new JSONObject();
        validated = false;
        direction = REST;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("player", player);
        result.put("type", new JSONNumber(type));
        result.put("direction", new JSONNumber(direction));
        result.put("validated", JSONBoolean.getInstance(validated));

        return result;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        reset();

        player = jsonObject.get("player").isObject();
        direction = (int)jsonObject.get("direction").isNumber().doubleValue();
        validated = jsonObject.get("validated").isBoolean().booleanValue();
    }

    public JSONObject getPlayer() {
        return player;
    }

    public void setPlayer(JSONObject player) {
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