package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import org.json.JSONObject;

public class PlayerMoveCommand extends Command {
    private static final int MOVE_UP = 0x01;
    private static final int MOVE_LEFT = 0x02;
    private static final int MOVE_DOWN = 0x03;
    private static final int MOVE_RIGHT = 0x04;

    private JSONObject player;
    private int direction;

    public PlayerMoveCommand() {
        reset();
    }

    public PlayerMoveCommand(JSONObject player, int direction) {
        this.type = Commands.PLAYER_MOVE_COMMAND;
        this.player = player;
        this.direction = direction;
    }

    @Override
    public void reset() {
        this.type = Commands.PLAYER_MOVE_COMMAND;
        this.player = new JSONObject();
        this.type = MOVE_UP;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("player", player);
        result.put("type", type);
        result.put("direction", direction);

        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        reset();

        player = jsonObject.getJSONObject("player");
        direction = jsonObject.getInt("direction");
    }
}