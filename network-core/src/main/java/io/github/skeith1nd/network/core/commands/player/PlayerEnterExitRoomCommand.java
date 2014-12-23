package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import org.json.JSONObject;

public class PlayerEnterExitRoomCommand extends Command {
    private boolean enter;
    private JSONObject player;
    private String roomId;

    public PlayerEnterExitRoomCommand() {
        reset();
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("type", type);
        result.put("enter", enter);
        result.put("roomId", roomId);
        result.put("player", player);

        return result;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        reset();

        enter = jsonObject.getBoolean("enter");
        roomId = jsonObject.getString("roomId");
        player = jsonObject.getJSONObject("player");
    }

    @Override
    public void reset() {
        type = Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND;
        enter = true;
        player = new JSONObject();
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public JSONObject getPlayer() {
        return player;
    }

    public void setPlayer(JSONObject player) {
        this.player = player;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
