package io.github.skeith1nd.network.core.room;

import io.github.skeith1nd.network.core.INetworkObject;
import io.github.skeith1nd.network.core.player.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Room implements INetworkObject {
    private Map<String, Player> players;
    private String roomId;

    public Room() {
        reset();
    }

    @Override
    public void reset() {
        players = new HashMap<String, Player>();
        roomId = "";
    }

    @Override
    public JSONObject serialize() {
        JSONObject result = new JSONObject();
        result.put("players", players);
        result.put("roomId", roomId);
        return result;
    }

    @Override
    public void deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject playerJSONMap = jsonObject.getJSONObject("players");

        reset();

        players = toMap(playerJSONMap);
        roomId = jsonObject.getString("roomId");
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
