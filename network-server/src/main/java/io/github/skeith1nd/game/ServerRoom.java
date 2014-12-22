package io.github.skeith1nd.game;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ServerRoom {
    private String roomId;
    private HashMap<String, ServerPlayer> players;

    public ServerRoom(String roomId) {
        this.roomId = roomId;
        players = new HashMap<String, ServerPlayer>();
    }

    public void fromJSON(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = new JSONArray(jsonObject.getString("players"));
        roomId = jsonObject.getString("roomId");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ServerPlayer serverPlayer = new ServerPlayer();
            serverPlayer.fromJSON(obj);
            players.put(serverPlayer.getUserId(), serverPlayer);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", roomId);

        JSONArray jsonArray = new JSONArray();
        for (ServerPlayer serverPlayer : players.values()) {
            jsonArray.put(serverPlayer.toJSON());
        }

        jsonObject.put("players", jsonArray);
        return jsonObject;
    }

    public HashMap<String, ServerPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, ServerPlayer> players) {
        this.players = players;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
