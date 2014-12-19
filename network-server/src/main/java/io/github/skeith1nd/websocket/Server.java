package io.github.skeith1nd.websocket;

import io.github.skeith1nd.data.Database;
import io.github.skeith1nd.network.core.INetworkObject;
import io.github.skeith1nd.network.core.commands.Commands;
import io.github.skeith1nd.network.core.commands.player.PlayerLoginCommand;
import io.github.skeith1nd.network.core.player.Player;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

public class Server extends WebSocketServer {
    public Server( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
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
        // User disconnects
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
                playerLoginCommand.setPlayer(Database.getInstance().getPlayer(playerLoginCommand.getUserId()));
                playerLoginCommand.setSuccess(true);

                // Send login response
                conn.send(playerLoginCommand.serialize().toString());
                break;
        }
    }

    @Override
    public void onFragment( WebSocket conn, Framedata fragment ) {
        // Not sure
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

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text
     *            The String to send across the network.
     * @throws InterruptedException
     *             When socket related I/O errors occur.
     */
    public void sendToAll( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }
}
