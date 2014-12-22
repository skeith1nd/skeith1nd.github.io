package io.github.skeith1nd.core.world;

import io.github.skeith1nd.core.player.ClientPlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ClientRoom {
    private String roomId;
    private HashMap<String, ClientPlayer> players;

    public ClientRoom() {
        players = new HashMap<String, ClientPlayer>();
    }

    public void fromJSON(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("players");
        roomId = jsonObject.getString("roomId");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ClientPlayer clientPlayer = new ClientPlayer();
            clientPlayer.fromJSON(obj);
            clientPlayer.init();
            players.put(clientPlayer.getUserId(), clientPlayer);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", roomId);

        JSONArray jsonArray = new JSONArray();
        for (ClientPlayer clientPlayer : players.values()) {
            jsonArray.put(clientPlayer);
        }

        jsonObject.put("players", jsonArray);
        return jsonObject;
    }

    public void addOrUpdatePlayer(JSONObject player) {
        ClientPlayer clientPlayer = new ClientPlayer();
        clientPlayer.fromJSON(player);
        clientPlayer.init();
        players.put(clientPlayer.getUserId(), clientPlayer);
    }

    public void removePlayer(String userId) {
        players.remove(userId);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public HashMap<String, ClientPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, ClientPlayer> players) {
        this.players = players;
    }
}
