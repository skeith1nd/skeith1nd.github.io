package io.github.skeith1nd.game;

import playn.core.Json;
import playn.core.PlayN;

import java.util.HashMap;

public class ServerRoom {
    private String roomId;
    private HashMap<String, ServerPlayer> players;

    public ServerRoom(String roomId) {
        this.roomId = roomId;
        players = new HashMap<String, ServerPlayer>();
    }

    public void fromJSON(String jsonString) {
        Json.Object jsonObject = PlayN.json().parse(jsonString);
        Json.Array jsonArray = jsonObject.getArray("players");

        roomId = jsonObject.getString("roomId");

        for (int i = 0; i < jsonArray.length(); i++) {
            Json.Object obj = jsonArray.getObject(i);
            ServerPlayer serverPlayer = new ServerPlayer();
            serverPlayer.fromJSON(obj);
            players.put(serverPlayer.getUserId(), serverPlayer);
        }
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = PlayN.json().createObject();
        jsonObject.put("roomId", roomId);

        Json.Array jsonArray = PlayN.json().createArray();
        for (ServerPlayer serverPlayer : players.values()) {
            jsonArray.set(jsonArray.length(), serverPlayer.toJSON());
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
