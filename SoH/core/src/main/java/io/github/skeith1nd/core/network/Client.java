package io.github.skeith1nd.core.network;

import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.core.util.JSONUtil;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import playn.core.Json;
import playn.core.Net;
import playn.core.PlayN;

import java.nio.ByteBuffer;

import static playn.core.PlayN.json;
import static playn.core.PlayN.net;

public class Client {
    private static Client instance;
    private String websocketServerAddress = "ws://192.168.0.15:8887";
    private Net.WebSocket socket;
    private boolean connected = false;

    private Client() {}

    public void connect() {
        socket = net().createWebSocket(websocketServerAddress, new Net.WebSocket.Listener() {
            @Override
            public void onOpen() {
                // Connected the websocket. Now attempt to "login" using User credentials
                PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                playerLoginCommand.setUserId("skeith1nd");
                socket.send(JSONUtil.serialize(playerLoginCommand));
                connected = true;
            }

            @Override
            public void onTextMessage(String message) {
                // Handle message from server
                Json.Object jsonObject = json().parse(message);
                switch (jsonObject.getInt("type")) {
                    case Commands.PLAYER_LOGIN_COMMAND:
                        System.out.println("Player login command");

                        // Get the player login command
                        PlayerLoginCommand playerLoginCommand = (PlayerLoginCommand)JSONUtil.deserialize(Commands.PLAYER_LOGIN_COMMAND, jsonObject);

                        // Init the game with the user information returned from the login server
                        Player.getInstance().fromJSON(PlayN.json().parse(playerLoginCommand.getPlayerJson()), PlayN.json().parse(playerLoginCommand.getRoomJson()));
                        Player.getInstance().init();
                        break;
                    case Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND:
                        System.out.println("player enter/exit room");

                        // Get the player enter/exit command
                        PlayerEnterExitRoomCommand playerEnterExitRoomCommand = (PlayerEnterExitRoomCommand)JSONUtil.deserialize(Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND, jsonObject);

                        // Get player id
                        String playerId = PlayN.json().parse(playerEnterExitRoomCommand.getPlayerJson()).getString("userId");

                        // If another player entered/exit the room, update player's room object
                        if (!playerId.equals(Player.getInstance().getUserId())) {
                            if (playerEnterExitRoomCommand.isEnter()) {
                                Player.getInstance().getRoom().addOrUpdatePlayer(PlayN.json().parse(playerEnterExitRoomCommand.getPlayerJson()));
                            } else {
                                Player.getInstance().getRoom().removePlayer(playerId);
                            }
                        }
                        break;
                    case Commands.PLAYER_MOVE_COMMAND:
                        System.out.println("player move command");

                        // Get the move command
                        PlayerMoveCommand playerMoveCommand = (PlayerMoveCommand)JSONUtil.deserialize(Commands.PLAYER_MOVE_COMMAND, jsonObject);
                        Json.Object playerJsonObject = PlayN.json().parse(playerMoveCommand.getPlayerJson());

                        // Process
                        if (playerJsonObject.getString("userId").equals(Player.getInstance().getUserId())) {
                            // If invalid, snap player back to last valid position
                            if (!playerMoveCommand.isValidated()) {
                                Player.getInstance().setX(playerJsonObject.getInt("x"));
                                Player.getInstance().setY(playerJsonObject.getInt("y"));
                            }
                        } else {
                            // If valid, send move command to client player
                            if (playerMoveCommand.isValidated()) {
                                String userId = playerJsonObject.getString("userId");
                                ClientPlayer clientPlayer = Player.getInstance().getRoom().getPlayers().get(userId);

                                switch (playerMoveCommand.getDirection()) {
                                    case PlayerMoveCommand.MOVE_UP:
                                        clientPlayer.moveUp();
                                        break;
                                    case PlayerMoveCommand.MOVE_LEFT:
                                        clientPlayer.moveLeft();
                                        break;
                                    case PlayerMoveCommand.MOVE_DOWN:
                                        clientPlayer.moveDown();
                                        break;
                                    case PlayerMoveCommand.MOVE_RIGHT:
                                        clientPlayer.moveRight();
                                        break;
                                    case PlayerMoveCommand.REST:
                                        clientPlayer.rest();
                                        break;
                                }
                            }
                        }
                        break;
                }
            }

            @Override
            public void onDataMessage(ByteBuffer msg) {
                System.out.println(msg);
            }

            @Override
            public void onClose() {
                System.out.println("closed");
                connected = false;
            }

            @Override
            public void onError(String reason) {
                System.out.println("reason: " + reason);
            }
        });
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void send(Json.Object jsonObject) {
        if (connected) {
            socket.send(json().newWriter().object(jsonObject).write());
        }
    }

    public void sendPlayerMoveCommand(int direction) {
        if (connected) {
            PlayerMoveCommand playerMoveCommand = new PlayerMoveCommand();
            playerMoveCommand.setDirection(direction);
            playerMoveCommand.setPlayerJson(JSONUtil.serialize(Player.getInstance()));
            socket.send(JSONUtil.serialize(playerMoveCommand));
        }
    }
}
