package io.github.skeith1nd.data;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import io.github.skeith1nd.game.ServerPlayer;

public class Database {
    private static Database instance;
    private Database(){}

    private String data = "{\n" +
            "    \"skeith1nd\" : {\n" +
            "        \"name\" : \"steve\",\n" +
            "        \"level\" : 1,\n" +
            "        \"roomId\" : \"1\",\n" +
            "        \"x\" : 200,\n" +
            "        \"y\" : 200,\n" +
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
        JSONObject jsonObject = new JSONObject(JsonUtils.safeEval(data));
        JSONObject jsonUser = (JSONObject)jsonObject.get(userId);

        ServerPlayer result = new ServerPlayer();

        result.setUserId(userId);
        result.setType(((JSONString)jsonUser.get("type")).stringValue());
        result.setX((int)((JSONNumber)jsonUser.get("x")).doubleValue());
        result.setY((int)((JSONNumber)jsonUser.get("y")).doubleValue());
        result.setRoomId(((JSONString)jsonUser.get("roomId")).stringValue());

        return result;
    }
}
