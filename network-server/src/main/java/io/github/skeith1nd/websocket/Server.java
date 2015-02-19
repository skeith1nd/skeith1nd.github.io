package io.github.skeith1nd.websocket;

import io.github.skeith1nd.data.Database;
import io.github.skeith1nd.game.CommandProcessor;
import io.github.skeith1nd.game.Engine;
import io.github.skeith1nd.game.ServerPlayer;
import io.github.skeith1nd.game.ServerRoom;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import io.github.skeith1nd.util.JSONUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends WebSocketServer {
    private static Server instance;
    private HashMap<WebSocket, ServerPlayer> players = new HashMap<WebSocket, ServerPlayer>();

    private int temp = 0;

    private Server( int port ) {
        super( new InetSocketAddress( port ) );

        Engine.getInstance().init();
        CommandProcessor.getInstance().start();
        Database.getInstance().init();
    }

    public static Server getInstance() {
        if (instance == null) instance = new Server(8887);
        return instance;
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        // User connects - handle adding connections when client login command comes in
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        // User disconnects, send exit room command to all other players in room
        ServerPlayer player = players.remove(conn);
        System.out.println("Player " + player.getUserId() + " has disconnected.");

        // Remove from server room
        ServerRoom serverRoom = Engine.getInstance().getRooms().get(player.getRoomId());
        serverRoom.getPlayers().put(player.getUserId(), player);

        // Send player exit room to all players in room
        PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
        playerEnterExitRoomCommand.setEnter(false);
        playerEnterExitRoomCommand.setPlayerJson(player.toJSON().toString());
        playerEnterExitRoomCommand.setRoomId(player.getRoomId());
        sendToAllInRoom(player.getRoomId(), JSONUtil.serialize(playerEnterExitRoomCommand));

        // Save player
        Database.getInstance().savePlayer(player);
    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
        JSONObject jsonObject = new JSONObject(message);
        switch (jsonObject.getInt("type")) {
            case Commands.PLAYER_LOGIN_COMMAND:
                // Get the player login command
                PlayerLoginCommand playerLoginCommand = (PlayerLoginCommand)JSONUtil.deserialize(Commands.PLAYER_LOGIN_COMMAND, jsonObject);

                // Access database to get player object to send to user
                if (temp % 2 == 0) { playerLoginCommand.setUserId("skeith1nd"); }
                if (temp % 2 == 1) { playerLoginCommand.setUserId("danielpuder"); }
                temp++;
                ServerPlayer serverPlayer = Database.getInstance().getPlayer(playerLoginCommand.getUserId());
                serverPlayer.setWebSocket(conn);
                players.put(conn, serverPlayer);

                // Get the player room
                ServerRoom serverRoom = Engine.getInstance().getRooms().get(serverPlayer.getRoomId());
                serverRoom.getPlayers().put(serverPlayer.getUserId(), serverPlayer);

                playerLoginCommand.setRoomJson(serverRoom.toJSON().toString());
                playerLoginCommand.setPlayerJson(serverPlayer.toJSON().toString());
                playerLoginCommand.setSuccess(true);

                // Send login response
                conn.send(JSONUtil.serialize(playerLoginCommand));

                // Send player enter room to all players in room
                PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
                playerEnterExitRoomCommand.setEnter(true);
                playerEnterExitRoomCommand.setPlayerJson(serverPlayer.toJSON().toString());
                playerEnterExitRoomCommand.setRoomId(serverPlayer.getRoomId());
                sendToAllInRoom(serverPlayer.getRoomId(), JSONUtil.serialize(playerEnterExitRoomCommand));
                break;
            case Commands.PLAYER_MOVE_COMMAND:
                // Get the player move command
                PlayerMoveCommand playerMoveCommand = (PlayerMoveCommand)JSONUtil.deserialize(Commands.PLAYER_MOVE_COMMAND, jsonObject);

                // Add to processing queue
                CommandProcessor.getInstance().getCommands().add(playerMoveCommand);
                break;
        }
    }

    public static void main( String[] args ) throws InterruptedException , IOException {
        WebSocketImpl.DEBUG = true;


        Server.getInstance().start();
        System.out.println("Server started on port: " + Server.getInstance().getPort());

        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        while ( true ) {
            String in = sysin.readLine();
            if( in.equals( "exit" ) ) {
                Server.getInstance().stop();
                break;
            } else if( in.equals( "restart" ) ) {
                Server.getInstance().stop();
                Server.getInstance().start();
                break;
            }
        }
    }
    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public void sendToAllInRoom(String roomId, String jsonString) {
        ServerRoom room = Engine.getInstance().getRooms().get(roomId);
        HashMap<String, ServerPlayer> players = room.getPlayers();
        for (ServerPlayer player : players.values()) {
            player.getWebSocket().send(jsonString.toString());
        }
    }
}
