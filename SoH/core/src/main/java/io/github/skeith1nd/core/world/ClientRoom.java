package io.github.skeith1nd.core.world;

import static playn.core.PlayN.*;

import io.github.skeith1nd.core.player.ClientPlayer;
import io.github.skeith1nd.core.player.Player;
import playn.core.Json;

import java.util.HashMap;

public class ClientRoom {
    private String roomId;
    private HashMap<String, ClientPlayer> players;

    public ClientRoom() {
        players = new HashMap<String, ClientPlayer>();
    }

    public void fromJSON(Json.Object jsonObject) {
        Json.Array jsonArray = jsonObject.getArray("players");
        roomId = jsonObject.getString("roomId");

        for (int i = 0; i < jsonArray.length(); i++) {
            Json.Object obj = jsonArray.getObject(i);

            if (!obj.getString("userId").equals(Player.getInstance().getUserId())) {
                ClientPlayer clientPlayer = new ClientPlayer();
                clientPlayer.fromJSON(obj);
                clientPlayer.init();
                players.put(clientPlayer.getUserId(), clientPlayer);
            }
        }
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = json().createObject();
        jsonObject.put("roomId", roomId);

        Json.Array jsonArray = json().createArray();
        for (ClientPlayer clientPlayer : players.values()) {
            jsonArray.set(jsonArray.length(), clientPlayer.toJSON());
        }

        jsonObject.put("players", jsonArray);
        return jsonObject;
    }

    public ClientPlayer addOrUpdatePlayer(Json.Object player) {
        ClientPlayer clientPlayer = new ClientPlayer();
        clientPlayer.fromJSON(player);
        clientPlayer.init();
        players.put(clientPlayer.getUserId(), clientPlayer);
        return clientPlayer;
    }

    public ClientPlayer removePlayer(String userId) {
        return players.remove(userId);
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
