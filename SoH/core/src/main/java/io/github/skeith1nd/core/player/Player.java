package io.github.skeith1nd.core.player;

import static playn.core.PlayN.*;

import io.github.skeith1nd.core.network.Client;
import io.github.skeith1nd.core.world.ClientRoom;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import playn.core.AssetWatcher;
import playn.core.Image;
import playn.core.Json;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private static Player instance;

    private Image spriteSheet;
    private HashMap<String, ArrayList<Image>> animations;
    private int x, y;
    private double currentSpriteIndex;
    private byte control;
    private boolean loaded, resting;
    private String type, userId;
    private ClientRoom room;

    private Player() {
        x = y = 0;
        type = userId = "";
        currentSpriteIndex = 0d;
        control = 0x08;
        loaded = false;
        resting = true;
    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public void fromJSON(Json.Object playerJSON, Json.Object roomJSON) {
        x = playerJSON.getInt("x");
        y = playerJSON.getInt("y");
        type = playerJSON.getString("type");
        userId = playerJSON.getString("userId");

        room = new ClientRoom();
        room.fromJSON(roomJSON);
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = json().createObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("type", type);
        jsonObject.put("userId", userId);

        if (room != null) {
            jsonObject.put("room", room.toJSON());
        }

        return jsonObject;
    }

    public void init() {
        spriteSheet = assets().getImage("images/characters/" + type + "/" + type + ".png");
        animations = new HashMap<String, ArrayList<Image>>();

        // Load animations after assets loaded successfully TODO: move this into main asset watcher
        AssetWatcher assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
            @Override
            public void done() {
                loadAnimations();
            }

            @Override
            public void error(Throwable e) {

            }
        });
        assetWatcher.add(spriteSheet);
        assetWatcher.start();
    }

    // TODO: load animation information from a data file
    private void loadAnimations() {
        // Load walk up animation
        ArrayList<Image> walkUp = new ArrayList<Image>();
        for (int i = 0; i < 9; i++) {
            walkUp.add(spriteSheet.subImage(
                    i * 64,
                    8 * 64,
                    64,
                    64
            ));
        }
        animations.put("walk_up", walkUp);

        // Load walk left animation
        ArrayList<Image> walkLeft = new ArrayList<Image>();
        for (int i = 0; i < 9; i++) {
            walkLeft.add(spriteSheet.subImage(
                    i * 64,
                    9 * 64,
                    64,
                    64
            ));
        }
        animations.put("walk_left", walkLeft);

        // Load walk down animation
        ArrayList<Image> walkDown = new ArrayList<Image>();
        for (int i = 0; i < 9; i++) {
            walkDown.add(spriteSheet.subImage(
                    i * 64,
                    10 * 64,
                    64,
                    64
            ));
        }
        animations.put("walk_down", walkDown);

        // Load walk right animation
        ArrayList<Image> walkRight = new ArrayList<Image>();
        for (int i = 0; i < 9; i++) {
            walkRight.add(spriteSheet.subImage(
                    i * 64,
                    11 * 64,
                    64,
                    64
            ));
        }
        animations.put("walk_right", walkRight);

        loaded = true;
    }

    public void moveUp(){
        y -= 3;
        control = 0x01;
        incrementSpriteIndex();

        // Send move command to server
        Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_UP);
    }

    public void moveLeft(){
        x -= 3;
        control = 0x02;
        incrementSpriteIndex();

        // Send move command to server
        Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_LEFT);
    }

    public void moveDown(){
        y += 3;
        control = 0x04;
        incrementSpriteIndex();

        // Send move command to server
        Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_DOWN);
    }

    public void moveRight(){
        x += 3;
        control = 0x08;
        incrementSpriteIndex();

        // Send move command to server
        Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_RIGHT);
    }

    public void rest() {
        currentSpriteIndex = 0d;

        // Send move command to server if the last move wasn't resting
        if (!resting) {
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.REST);
            resting = true;
        }
    }

    public Image getCurrentImage() {
        switch (control) {
            case 0x01:
                return animations.get("walk_up").get((int)currentSpriteIndex / 10);
            case 0x02:
                return animations.get("walk_left").get((int)currentSpriteIndex / 10);
            case 0x04:
                return animations.get("walk_down").get((int)currentSpriteIndex / 10);
            case 0x08:
                return animations.get("walk_right").get((int)currentSpriteIndex / 10);
        }
        return animations.get("walk_up").get((int)currentSpriteIndex / 10);
    }

    private void incrementSpriteIndex() {
        currentSpriteIndex += 2;
        if (currentSpriteIndex >= 90) {
            currentSpriteIndex = 0.0;
        }
    }

    public Image getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(Image spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public HashMap<String, ArrayList<Image>> getAnimations() {
        return animations;
    }

    public void setAnimations(HashMap<String, ArrayList<Image>> animations) {
        this.animations = animations;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getCurrentSpriteIndex() {
        return currentSpriteIndex;
    }

    public void setCurrentSpriteIndex(double currentSpriteIndex) {
        this.currentSpriteIndex = currentSpriteIndex;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public byte getControl() {
        return control;
    }

    public void setControl(byte control) {
        this.control = control;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ClientRoom getRoom() {
        return room;
    }

    public void setRoom(ClientRoom room) {
        this.room = room;
    }
}
