package io.github.skeith1nd.game;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import java.util.HashMap;

public class ServerRoom {
    private String roomId;
    private HashMap<String, ServerPlayer> players;

    public ServerRoom(String roomId) {
        this.roomId = roomId;
        players = new HashMap<String, ServerPlayer>();
    }

    public void fromJSON(String jsonString) {
        JSONObject jsonObject = new JSONObject(JsonUtils.safeEval(jsonString));
        JSONArray jsonArray = jsonObject.get("players").isArray();

        roomId = jsonObject.get("roomId").isString().stringValue();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.get(i).isObject();
            ServerPlayer serverPlayer = new ServerPlayer();
            serverPlayer.fromJSON(obj);
            players.put(serverPlayer.getUserId(), serverPlayer);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", new JSONString(roomId));

        JSONArray jsonArray = new JSONArray();
        for (ServerPlayer serverPlayer : players.values()) {
            jsonArray.set(jsonArray.size(), serverPlayer.toJSON());
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
