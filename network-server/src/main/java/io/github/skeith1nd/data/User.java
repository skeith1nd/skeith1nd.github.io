package io.github.skeith1nd.data;

import io.github.skeith1nd.network.core.player.Player;
import org.java_websocket.WebSocket;

public class User {
    private WebSocket webSocket;
    private Player player;

    public User(WebSocket webSocket, Player player) {
        this.webSocket = webSocket;
        this.player = player;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
