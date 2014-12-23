package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import org.json.JSONObject;

public class PlayerLoginCommand extends Command {
    private String userId;
    private boolean success;
    private JSONObject player, room;

    public PlayerLoginCommand() {
        reset();
    }

    public PlayerLoginCommand(String userId) {
        this.success = false;
        this.player = new JSONObject();
        this.room = new JSONObject();
        this.userId = userId;
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("userId", userId);
        result.put("type", type);
        result.put("success", success);
        result.put("player", player);
        result.put("room", room);

        return result;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        reset();

        userId = jsonObject.getString("userId");
        success = jsonObject.getBoolean("success");
        player = jsonObject.getJSONObject("player");
        room = jsonObject.getJSONObject("room");
    }

    @Override
    public void reset() {
        userId = "";
        success = false;
        player = new JSONObject();
        room = new JSONObject();
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

    public JSONObject getPlayer() {
        return player;
    }

    public void setPlayer(JSONObject player) {
        this.player = player;
    }

    public JSONObject getRoom() {
        return room;
    }

    public void setRoom(JSONObject room) {
        this.room = room;
    }
}
