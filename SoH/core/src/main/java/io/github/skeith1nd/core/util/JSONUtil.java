package io.github.skeith1nd.core.util;

import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.core.world.ClientRoom;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import playn.core.Json;
import playn.core.PlayN;

public class JSONUtil {
    public static Object deserialize(int type, Json.Object jsonObject) {
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
        Json.Writer writer = PlayN.json().newWriter();
        writer.object();
        writer.value("userId", playerLoginCommand.getUserId());
        writer.value("success", playerLoginCommand.isSuccess());
        writer.value("type", playerLoginCommand.getType());
        writer.value("player", playerLoginCommand.getPlayerJson());
        writer.value("room", playerLoginCommand.getRoomJson());
        writer.end();
        return writer.write();
    }

    public static String serialize(PlayerEnterExitRoomCommand playerEnterExitRoomCommand) {
        Json.Writer writer = PlayN.json().newWriter();
        writer.object();
        writer.value("roomId", playerEnterExitRoomCommand.getRoomId());
        writer.value("enter", playerEnterExitRoomCommand.isEnter());
        writer.value("type", playerEnterExitRoomCommand.getType());
        writer.value("player", playerEnterExitRoomCommand.getPlayerJson());
        writer.end();
        return writer.write();
    }

    public static String serialize(PlayerMoveCommand playerMoveCommand) {
        Json.Writer writer = PlayN.json().newWriter();
        writer.object();
        writer.value("player", playerMoveCommand.getPlayerJson());
        writer.value("direction", playerMoveCommand.getDirection());
        writer.value("validated", playerMoveCommand.isValidated());
        writer.value("type", playerMoveCommand.getType());
        writer.end();
        return writer.write();
    }

    public static String serialize(Player player) {
        Json.Writer writer = PlayN.json().newWriter();
        writer.object();
        writer.value("x", player.getX());
        writer.value("y", player.getY());
        writer.value("userId", player.getUserId());
        writer.value("type", player.getType());

        if (player.getRoom() != null) {
            writer.value("roomId", player.getRoom().getRoomId());
        }

        writer.end();
        return writer.write();
    }

    public static String serialize(ClientRoom clientRoom) {
        Json.Writer writer = PlayN.json().newWriter();
        writer.object();
        writer.value("roomId", clientRoom.getRoomId());

        writer.array();
        for (ClientPlayer clientPlayer : clientRoom.getPlayers().values()) {
            writer.object();
            writer.value("x", clientPlayer.getX());
            writer.value("y", clientPlayer.getY());
            writer.value("userId", clientPlayer.getUserId());
            writer.value("type", clientPlayer.getType());
            writer.value("roomId", clientPlayer.getRoomId());
            writer.end();
        }
        writer.end();

        writer.end();
        return writer.write();
    }
}
