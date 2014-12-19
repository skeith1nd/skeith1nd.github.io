package io.github.skeith1nd.core.room;

import io.github.skeith1nd.network.core.player.Player;

import java.util.ArrayList;

public class Room {
    private int roomId;
    private ArrayList<Player> players;

    public Room() {
        players = new ArrayList<Player>();
    }
}
