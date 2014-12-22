package io.github.skeith1nd.core.network;

import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.core.world.World;
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
                playerLoginCommand.setUserId("skeith1nd");
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
                        Player.getInstance().fromJSON(playerLoginCommand.getPlayer(), playerLoginCommand.getRoom());
                        Player.getInstance().init();
                        break;
                    case Commands.PLAYER_ENTER_EXIT_ROOM_COMMAND:
                        System.out.println("player enter/exit room");

                        // Get the player enter/exit command
                        PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
                        playerEnterExitRoomCommand.deserialize(jsonObject.toString());

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
