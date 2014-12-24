package io.github.skeith1nd.core.network;

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
                socket.send(json().newWriter().object(playerLoginCommand.serialize()).write());
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
                        String playerId = playerEnterExitRoomCommand.getPlayer().getString("userId");

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
                        if (playerMoveCommand.getPlayer().getString("userId").equals(Player.getInstance().getUserId())) {
                            // If invalid, snap player back to last valid position
                            if (!playerMoveCommand.isValidated()) {
                                Player.getInstance().setX(playerMoveCommand.getPlayer().getInt("x"));
                                Player.getInstance().setY(playerMoveCommand.getPlayer().getInt("y"));
                            }
                        } else {
                            // If valid, send move command to client player
                            if (playerMoveCommand.isValidated()) {
                                String userId = playerMoveCommand.getPlayer().getString("userId");
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

    public void send(Json.Object jsonObject) {
        if (connected) {
            socket.send(json().newWriter().object(jsonObject).write());
        }
    }

    public void sendPlayerMoveCommand(int direction) {
        if (connected) {
            PlayerMoveCommand playerMoveCommand = new PlayerMoveCommand(Player.getInstance().toJSON(), direction);
            socket.send(json().newWriter().object(playerMoveCommand.serialize()).write());
        }
    }
}
