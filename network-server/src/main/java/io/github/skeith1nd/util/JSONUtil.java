package io.github.skeith1nd.util;

import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import org.json.JSONObject;

public class JSONUtil {
    public static Object deserialize(int type, JSONObject jsonObject) {
        switch (type) {
            case Commands.PLAYER_LOGIN_COMMAND:
                PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                playerLoginCommand.setUserId(jsonObject.getString("userId"));
                playerLoginCommand.setSuccess(jsonObject.getBoolean("success"));
                playerLoginCommand.setPlayerJson(jsonObject.getString("player"));
                playerLoginCommand.setRoomJson(jsonObject.getString("room"));
                playerLoginCommand.setType(jsonObject.getInt("type"));
                return playerLoginCommand;
            case Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND:
                PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
                playerEnterExitRoomCommand.setRoomId(jsonObject.getString("roomId"));
                playerEnterExitRoomCommand.setEnter(jsonObject.getBoolean("enter"));
                playerEnterExitRoomCommand.setPlayerJson(jsonObject.getString("player"));
                playerEnterExitRoomCommand.setType(jsonObject.getInt("type"));
                return playerEnterExitRoomCommand;
            case Commands.PLAYER_MOVE_COMMAND:
                PlayerMoveCommand playerMoveCommand = new PlayerMoveCommand();
                playerMoveCommand.setPlayerJson(jsonObject.getString("player"));
                playerMoveCommand.setDirection(jsonObject.getInt("direction"));
                playerMoveCommand.setValidated(jsonObject.getBoolean("validated"));
                playerMoveCommand.setType(jsonObject.getInt("type"));
                return playerMoveCommand;
        }
        return null;
    }

    public static String serialize(PlayerLoginCommand playerLoginCommand) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", playerLoginCommand.getUserId());
        jsonObject.put("type", playerLoginCommand.getType());
        jsonObject.put("success", playerLoginCommand.isSuccess());
        jsonObject.put("player", playerLoginCommand.getPlayerJson());
        jsonObject.put("room", playerLoginCommand.getRoomJson());
        return jsonObject.toString();
    }

    public static String serialize(PlayerEnterExitRoomCommand playerEnterExitRoomCommand) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", playerEnterExitRoomCommand.getRoomId());
        jsonObject.put("enter", playerEnterExitRoomCommand.isEnter());
        jsonObject.put("type", playerEnterExitRoomCommand.getType());
        jsonObject.put("player", playerEnterExitRoomCommand.getPlayerJson());
        return jsonObject.toString();
    }

    public static String serialize(PlayerMoveCommand playerMoveCommand) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player", playerMoveCommand.getPlayerJson());
        jsonObject.put("direction", playerMoveCommand.getDirection());
        jsonObject.put("validated", playerMoveCommand.isValidated());
        jsonObject.put("type", playerMoveCommand.getType());
        return jsonObject.toString();
    }
}
