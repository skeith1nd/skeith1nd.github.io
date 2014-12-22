package io.github.skeith1nd.network.core.commands.player;

import io.github.skeith1nd.network.core.commands.Command;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.player.Player;
import org.json.JSONObject;

public class PlayerEnterExitRoomCommand extends Command {
    private boolean enter;
    private Player player;
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
        result.put("player", player.serialize());

        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        reset();

        enter = jsonObject.getBoolean("enter");
        roomId = jsonObject.getString("roomId");
        player.deserialize(jsonObject.getJSONObject("player").toString());
    }

    @Override
    public void reset() {
        type = Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND;
        enter = true;
        player = new Player();
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
