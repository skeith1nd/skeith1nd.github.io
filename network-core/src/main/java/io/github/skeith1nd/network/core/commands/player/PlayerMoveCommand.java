package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.INetworkObject;
import io.github.skeith1nd.network.core.commands.ICommand;
import io.github.skeith1nd.network.core.player.Player;
import org.json.JSONObject;

public class PlayerMoveCommand implements INetworkObject, ICommand {
    private static final int MOVE_UP = 0x01;
    private static final int MOVE_LEFT = 0x02;
    private static final int MOVE_DOWN = 0x03;
    private static final int MOVE_RIGHT = 0x04;

    private Player player;
    private int type;

    public PlayerMoveCommand() {
        reset();
    }

    public PlayerMoveCommand(Player player, byte type) {
        this.player = player;
        this.type = type;
    }

    @Override
    public void reset() {
        this.player = new Player();
        this.type = MOVE_UP;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("player", player.serialize());
        result.put("type", type);

        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        reset();

        player.deserialize(jsonObject.getJSONObject("player").toString());
        type = jsonObject.getInt("type");
    }
}