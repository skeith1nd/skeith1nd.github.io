package io.github.skeith1nd.core.network;

import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import org.json.JSONObject;
import playn.core.Net;

import java.nio.ByteBuffer;

import static playn.core.PlayN.net;

public class Client {
    private static Client instance;
    private String websocketServerAddress = "ws://localhost:8887";
    private Net.WebSocket socket;

    private Client() {}

    public void connect() {
        socket = net().createWebSocket(websocketServerAddress, new Net.WebSocket.Listener() {
            @Override
            public void onOpen() {
                // Connected the websocket. Now attempt to "login" using User credentials
                PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                playerLoginCommand.setUserId("danielpuder");
                socket.send(playerLoginCommand.serialize().toString());
            }

            @Override
            public void onTextMessage(String message) {
                // Handle message from server
                JSONObject jsonObject = new JSONObject(message);
                switch (jsonObject.getInt("type")) {
                    case Commands.PLAYER_LOGIN_COMMAND:
                        System.out.println("Player login command");

                        // Get the player login command
                        PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                        playerLoginCommand.deserialize(jsonObject.toString());

                        // Init the game with the user information returned from the login server
                        Player.getInstance().setUserId(playerLoginCommand.getPlayer().getUser());
                        Player.getInstance().setType(playerLoginCommand.getPlayer().getType());
                        Player.getInstance().setX(playerLoginCommand.getPlayer().getX());
                        Player.getInstance().setY(playerLoginCommand.getPlayer().getY());
                        Player.getInstance().setRoom(playerLoginCommand.getRoom());
                        Player.getInstance().init();
                        break;
                    case Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND:
                        System.out.println("player enter/exit room");

                        // Get the player enter/exit command
                        PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
                        playerEnterExitRoomCommand.deserialize(jsonObject.toString());

                        // If another player entered/exit the room, update player's room object
                        if (!playerEnterExitRoomCommand.getPlayer().getUser().equals(Player.getInstance().getUserId())) {
                            if (playerEnterExitRoomCommand.isEnter()) {
                                Player.getInstance().getRoom().getPlayers().put(
                                        playerEnterExitRoomCommand.getPlayer().getUser(),
                                        playerEnterExitRoomCommand.getPlayer()
                                );
                            } else {
                                Player.getInstance().getRoom().getPlayers().remove(playerEnterExitRoomCommand.getPlayer().getUser());
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
}
