package io.github.skeith1nd.core.world;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import io.github.skeith1nd.core.player.ClientPlayer;

import java.util.HashMap;

public class ClientRoom {
    private String roomId;
    private HashMap<String, ClientPlayer> players;

    public ClientRoom() {
        players = new HashMap<String, ClientPlayer>();
    }

    public void fromJSON(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.get("players").isArray();
        roomId = jsonObject.get("roomId").isString().stringValue();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.get(i).isObject();
            ClientPlayer clientPlayer = new ClientPlayer();
            clientPlayer.fromJSON(obj);
            clientPlayer.init();
            players.put(clientPlayer.getUserId(), clientPlayer);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", new JSONString(roomId));

        JSONArray jsonArray = new JSONArray();
        for (ClientPlayer clientPlayer : players.values()) {
            jsonArray.set(jsonArray.size(), clientPlayer.toJSON());
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
