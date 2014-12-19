package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.player.Player;
import org.json.JSONObject;

public class PlayerLoginCommand extends Command {
    private String userId;
    private boolean success;
    private Player player;

    public PlayerLoginCommand() {
        reset();
    }

    public PlayerLoginCommand(String userId) {
        this.success = false;
        this.player = new Player();
        this.userId = userId;
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("userId", userId);
        result.put("type", type);
        result.put("success", success);
        result.put("player", player.serialize());

        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        reset();

        userId = jsonObject.getString("userId");
        success = jsonObject.getBoolean("success");
        player.deserialize(jsonObject.getJSONObject("player").toString());
    }

    @Override
    public void reset() {
        userId = "";
        success = false;
        player = new Player();
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
