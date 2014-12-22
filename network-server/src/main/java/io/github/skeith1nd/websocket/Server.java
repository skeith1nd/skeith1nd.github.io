package io.github.skeith1nd.websocket;

import io.github.skeith1nd.data.Database;
import io.github.skeith1nd.game.ServerRoom;
import io.github.skeith1nd.data.User;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerEnterExitRoomCommand;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.player.Player;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Server extends WebSocketServer {
    private HashMap<String, ServerRoom> rooms = new HashMap<String, ServerRoom>();
    private HashMap<WebSocket, Player> connections = new HashMap<WebSocket, Player>();

    public Server( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );

        ServerRoom serverRoom = new ServerRoom("1");
        rooms.put(serverRoom.getRoomId(), serverRoom);
    }

    public Server( InetSocketAddress address ) {
        super( address );
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        // User connects
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        // User disconnects, send exit room command to all other players in room
        Player player = connections.get(conn);
        PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
        playerEnterExitRoomCommand.setEnter(false);
        playerEnterExitRoomCommand.setPlayer(player);
        playerEnterExitRoomCommand.setRoomId(player.getRoomId());
        sendToAllInRoom(player.getRoomId(), playerEnterExitRoomCommand.serialize().toString());
    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
        // User sends message
        JSONObject jsonObject = new JSONObject(message);
        switch (jsonObject.getInt("type")) {
            case Commands.PLAYER_LOGIN_COMMAND:
                // Get the player login command
                PlayerLoginCommand playerLoginCommand = new PlayerLoginCommand();
                playerLoginCommand.deserialize(jsonObject.toString());

                // Access database to get player object to send to user
                Player player = Database.getInstance().getPlayer(playerLoginCommand.getUserId());
                playerLoginCommand.setPlayer(player);
                playerLoginCommand.setSuccess(true);

                // Send login response
                conn.send(playerLoginCommand.serialize().toString());

                // Create user
                User user = new User(conn, player);
                connections.put(conn, player);

                // Add user to the room
                rooms.get(player.getRoomId()).getUsers().put(player.getUser(), user);

                // Send player enter room to all players in room
                PlayerEnterExitRoomCommand playerEnterExitRoomCommand = new PlayerEnterExitRoomCommand();
                playerEnterExitRoomCommand.setEnter(true);
                playerEnterExitRoomCommand.setPlayer(player);
                playerEnterExitRoomCommand.setRoomId(player.getRoomId());
                sendToAllInRoom(player.getRoomId(), playerEnterExitRoomCommand.serialize().toString());

                break;
        }
    }

    public static void main( String[] args ) throws InterruptedException , IOException {
        WebSocketImpl.DEBUG = true;
        int port = 8887;
        Server s = new Server( port );
        s.start();
        System.out.println( "Server started on port: " + s.getPort() );

        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        while ( true ) {
            String in = sysin.readLine();
            if( in.equals( "exit" ) ) {
                s.stop();
                break;
            } else if( in.equals( "restart" ) ) {
                s.stop();
                s.start();
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

    private void sendToAllInRoom(String roomId, String jsonMessage) {
        ServerRoom room = rooms.get(roomId);
        HashMap<String, User> users = room.getUsers();
        for (User user : users.values()) {
            user.getWebSocket().send(jsonMessage);
        }
    }
}
