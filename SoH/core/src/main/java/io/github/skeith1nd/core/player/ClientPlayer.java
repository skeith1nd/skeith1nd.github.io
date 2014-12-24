package io.github.skeith1nd.core.player;

import playn.core.AssetWatcher;
import playn.core.Image;
import playn.core.Json;

import java.util.ArrayList;
import java.util.HashMap;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.json;

public class ClientPlayer {
    private Image spriteSheet;
    private HashMap<String, ArrayList<Image>> animations;
    private int x = 320;
    private int y = 240;
    private double currentSpriteIndex = 0.0;
    private byte control = 0x08;
    private boolean loaded = false;
    private String type = "";
    private String userId = "";
    private String roomId = "";

    public void fromJSON(Json.Object jsonObject) {
        x = jsonObject.getInt("x");
        y = jsonObject.getInt("y");
        type = jsonObject.getString("type");
        roomId = jsonObject.getString("roomId");
        userId = jsonObject.getString("userId");
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = json().createObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("type", type);
        jsonObject.put("roomId", roomId);
        jsonObject.put("userId", userId);
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
    }

    public void moveLeft(){
        x -= 3;
        control = 0x02;
        incrementSpriteIndex();
    }

    public void moveDown(){
        y += 3;
        control = 0x04;
        incrementSpriteIndex();
    }

    public void moveRight(){
        x += 3;
        control = 0x08;
        incrementSpriteIndex();
    }

    public void rest() {
        currentSpriteIndex = 0d;
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
}
