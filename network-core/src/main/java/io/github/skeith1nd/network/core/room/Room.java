package io.github.skeith1nd.network.core.room;

import io.github.skeith1nd.network.core.INetworkObject;
import io.github.skeith1nd.network.core.player.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Room implements INetworkObject {
    private ArrayList<Player> players;

    public Room() {
        reset();
    }

    @Override
    public void reset() {
        players = new ArrayList<Player>();
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("players", players);
        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray playerJSONArray = jsonObject.getJSONArray("players");

        reset();

        for (int i = 0; i < playerJSONArray.length(); i++) {
            Player playerObject = new Player();
            playerObject.deserialize(playerJSONArray.getJSONObject(i).toString());
            players.add(playerObject);
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
