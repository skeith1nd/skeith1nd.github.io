package io.github.skeith1nd.core.network;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import playn.core.Json;
import playn.core.Net;

import java.nio.ByteBuffer;

import static playn.core.PlayN.json;
import static playn.core.PlayN.net;

public class Client {
    private static Client instance;
    private String websocketServerAddress = "ws://localhost:8887";
    private Net.WebSocket socket;
    private boolean connected = false;

    private Client() {}

    public void connect() {
        socket = net().createWebSocket(websocketServerAddress, new Net.WebSocket.Listener() {
            @Override
            public void onOpen() {
                // Connected the websocket. Now attempt to "login" using User credentials
                PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                playerLoginCommand.setUserId("danielpuder");
                socket.send(playerLoginCommand.serialize().toString());
                connected = true;
            }

            @Override
            public void onTextMessage(String message) {
                // Handle message from server
                JSONObject jsonObject = new JSONObject(JsonUtils.safeEval(message));
                switch ((int)jsonObject.get("type").isNumber().doubleValue()) {
                    case Commands.PLAYER_LOGIN_COMMAND:
                        System.out.println("Player login command");

                        // Get the player login command
                        PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                        playerLoginCommand.deserialize(jsonObject);

                        // Init the game with the user information returned from the login server
                        Player.getInstance().fromJSON(playerLoginCommand.getPlayer(), playerLoginCommand.getRoom());
                        Player.getInstance().init();
                        break;
                    case Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND:
                        System.out.println("player enter/exit room");

                        // Get the player enter/exit command
                        PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
                        playerEnterExitRoomCommand.deserialize(jsonObject);

                        // Get player id
                        String playerId = playerEnterExitRoomCommand.getPlayer().get("userId").isString().stringValue();

                        // If another player entered/exit the room, update player's room object
                        if (!playerId.equals(Player.getInstance().getUserId())) {
                            if (playerEnterExitRoomCommand.isEnter()) {
                                Player.getInstance().getRoom().addOrUpdatePlayer(playerEnterExitRoomCommand.getPlayer());
                            } else {
                                Player.getInstance().getRoom().removePlayer(playerId);
                            }
                        }
                        break;
                    case Commands.PLAYER_MOVE_COMMAND:
                        System.out.println("player move command");

                        // Get the move command
                        PlayerMoveCommand playerMoveCommand = new PlayerMoveCommand();
                        playerMoveCommand.deserialize(jsonObject);

                        // Process
                        if (playerMoveCommand.getPlayer().get("userId").isString().stringValue().equals(Player.getInstance().getUserId())) {
                            // If invalid, snap player back to last valid position
                            if (!playerMoveCommand.isValidated()) {
                                Player.getInstance().setX((int)playerMoveCommand.getPlayer().get("x").isNumber().doubleValue());
                                Player.getInstance().setY((int)playerMoveCommand.getPlayer().get("y").isNumber().doubleValue());
                            }
                        } else {
                            // If valid, send move command to client player
                            if (playerMoveCommand.isValidated()) {
                                String userId = playerMoveCommand.getPlayer().get("userId").isString().stringValue();
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

    public void send(JSONObject jsonObject) {
        if (connected) {
            socket.send(jsonObject.toString());
        }
    }

    public void sendPlayerMoveCommand(int direction) {
        if (connected) {
            PlayerMoveCommand playerMoveCommand = new PlayerMoveCommand(Player.getInstance().toJSON(), direction);
            socket.send(playerMoveCommand.serialize().toString());
        }
    }
}
