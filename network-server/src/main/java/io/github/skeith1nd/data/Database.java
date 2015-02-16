package io.github.skeith1nd.data;

import io.github.skeith1nd.game.ServerPlayer;
import org.json.JSONObject;

import java.io.*;

public class Database {
    private static Database instance;
    private Database() {}

    private JSONObject data;
    private String path;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void init() {
        path = "C:\\server\\users.txt";
        loadDatabase();
    }

    public ServerPlayer getPlayer(String userId) {
        JSONObject jsonUser = data.getJSONObject(userId);
        ServerPlayer result = new ServerPlayer();

        result.setUserId(userId);
        result.setType(jsonUser.getString("type"));
        result.setX(jsonUser.getInt("x"));
        result.setY(jsonUser.getInt("y"));
        result.setRoomId(jsonUser.getString("roomId"));
        result.setCurrentHP(jsonUser.getInt("currentHP"));
        result.setMaxHP(jsonUser.getInt("maxHP"));

        return result;
    }

    public void savePlayer(ServerPlayer player) {
        JSONObject jsonUser = data.getJSONObject(player.getUserId());
        jsonUser.put("x", player.getX());
        jsonUser.put("y", player.getY());
        jsonUser.put("roomId", player.getRoomId());
        jsonUser.put("currentHP", player.getCurrentHP());
        jsonUser.put("maxHP", player.getMaxHP());

        saveDatabase();
    }

    private void loadDatabase() {
        try {
            File file = new File(path);
            InputStream in = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            try {
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                data = new JSONObject(out.toString());
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDatabase() {
        try {
            FileWriter fw = new FileWriter(new File(path).getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data.toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
