package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import org.json.JSONObject;

public class PlayerEnterExitRoomCommand extends Command {
    private boolean enter;
    private String userId;

    public PlayerEnterExitRoomCommand() {
        reset();
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("type", type);
        result.put("enter", enter);
        result.put("userId", userId);

        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        reset();

        enter = jsonObject.getBoolean("enter");
        userId = jsonObject.getString("userId");
    }

    @Override
    public void reset() {
        type = Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND;
        enter = true;
        userId = "";
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
