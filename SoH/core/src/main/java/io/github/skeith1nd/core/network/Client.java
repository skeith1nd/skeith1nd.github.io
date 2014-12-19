package io.github.skeith1nd.core.network;

import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.network.core.commands.Commands;
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
                        // Get the player login command
                        PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                        playerLoginCommand.deserialize(jsonObject.toString());

                        // Init the game with the user information returned from the login server
                        Player.getInstance().setType(playerLoginCommand.getPlayer().getType());
                        Player.getInstance().setX(playerLoginCommand.getPlayer().getX());
                        Player.getInstance().setY(playerLoginCommand.getPlayer().getY());
                        Player.getInstance().init();
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
