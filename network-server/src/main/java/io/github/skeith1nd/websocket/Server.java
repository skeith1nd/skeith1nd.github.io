package io.github.skeith1nd.websocket;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import io.github.skeith1nd.data.Database;
import io.github.skeith1nd.game.CommandProcessor;
import io.github.skeith1nd.game.Engine;
import io.github.skeith1nd.game.ServerPlayer;
import io.github.skeith1nd.game.ServerRoom;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Server extends WebSocketServer {
    private static Server instance;
    private HashMap<String, ServerPlayer> connections = new HashMap<String, ServerPlayer>();

    private Server( int port ) {
        super( new InetSocketAddress( port ) );

        Engine.getInstance().init();
        CommandProcessor.getInstance().start();
    }

    public static Server getInstance() {
        if (instance == null) instance = new Server(8887);
        return instance;
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        // User connects
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        // User disconnects, send exit room command to all other players in room

    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
        JSONObject jsonObject = new JSONObject(JsonUtils.safeEval(message));
        switch ((int)jsonObject.get("type").isNumber().doubleValue()) {
            case Commands.PLAYER_LOGIN_COMMAND:
                // Get the player login command
                PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                playerLoginCommand.deserialize(jsonObject);

                // Access database to get player object to send to user
                ServerPlayer serverPlayer = Database.getInstance().getPlayer(playerLoginCommand.getUserId());
                serverPlayer.setWebSocket(conn);
                connections.put(serverPlayer.getUserId(), serverPlayer);

                // Get the player room
                ServerRoom serverRoom = Engine.getInstance().getRooms().get(serverPlayer.getRoomId());
                serverRoom.getPlayers().put(serverPlayer.getUserId(), serverPlayer);

                playerLoginCommand.setRoom(serverRoom.toJSON());
                playerLoginCommand.setPlayer(serverPlayer.toJSON());
                playerLoginCommand.setSuccess(true);

                // Send login response
                conn.send(playerLoginCommand.serialize().toString());

                // Send player enter room to all players in room
                PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
                playerEnterExitRoomCommand.setEnter(true);
                playerEnterExitRoomCommand.setPlayer(serverPlayer.toJSON());
                playerEnterExitRoomCommand.setRoomId(serverPlayer.getRoomId());
                sendToAllInRoom(serverPlayer.getRoomId(), playerEnterExitRoomCommand.serialize());
                break;
            case Commands.PLAYER_MOVE_COMMAND:
                // Get the player move command
                PlayerMoveCommand playerMoveCommand = new PlayerMoveCommand();
                playerMoveCommand.deserialize(jsonObject);

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

    public void sendToAllInRoom(String roomId, JSONObject jsonMessage) {
        ServerRoom room = Engine.getInstance().getRooms().get(roomId);
        HashMap<String, ServerPlayer> players = room.getPlayers();
        for (ServerPlayer player : players.values()) {
            player.getWebSocket().send(jsonMessage.toString());
        }
    }
}
