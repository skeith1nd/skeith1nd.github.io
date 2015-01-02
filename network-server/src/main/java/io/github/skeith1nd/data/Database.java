package io.github.skeith1nd.data;

import io.github.skeith1nd.game.ServerPlayer;
import org.json.JSONObject;

public class Database {
    private static Database instance;
    private Database(){}

    private String data = "{\n" +
            "    \"skeith1nd\" : {\n" +
            "        \"name\" : \"steve\",\n" +
            "        \"level\" : 1,\n" +
            "        \"roomId\" : \"1\",\n" +
            "        \"x\" : 5,\n" +
            "        \"y\" : 5,\n" +
            "        \"type\" : \"man1\"\n" +
            "    },\n" +
            "    \"danielpuder\" : {\n" +
            "        \"name\" : \"daniel\",\n" +
            "        \"level\" : 1,\n" +
            "        \"roomId\" : \"1\",\n" +
            "        \"x\" : 250,\n" +
            "        \"y\" : 220,\n" +
            "        \"type\" : \"man1\"\n" +
            "    }\n" +
            "}";

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public ServerPlayer getPlayer(String userId) {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonUser = jsonObject.getJSONObject(userId);

        ServerPlayer result = new ServerPlayer();

        result.setUserId(userId);
        result.setType(jsonUser.getString("type"));
        result.setX(jsonUser.getInt("x"));
        result.setY(jsonUser.getInt("y"));
        result.setRoomId(jsonUser.getString("roomId"));

        return result;
    }
}
