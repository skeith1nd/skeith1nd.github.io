package io.github.skeith1nd.core.player;

import io.github.skeith1nd.core.game.AssetManager;
import io.github.skeith1nd.core.util.TextUtil;
import io.github.skeith1nd.core.world.Renderable;
import io.github.skeith1nd.core.world.RenderedDynamic;
import io.github.skeith1nd.core.world.World;
import playn.core.*;

import java.util.ArrayList;
import java.util.HashMap;

import static playn.core.PlayN.json;

public class ClientPlayer extends RenderedDynamic {
    private Image spriteSheet, userIdText;
    private CanvasImage healthBar;
    private HashMap<String, ArrayList<Image>> animations;
    private double currentSpriteIndex = 0.0;
    private byte control = 0x08;
    private String type = "", userId = "", roomId = "";
    private int collisionWidth, collisionHeight;
    private PlayerStats stats;

    public ClientPlayer() {
        width = height = 64;
        collisionWidth = 20;
        collisionHeight = 10;
        stats = new PlayerStats();
    }

    public void fromJSON(Json.Object jsonObject) {
        x = jsonObject.getInt("x");
        y = jsonObject.getInt("y");
        type = jsonObject.getString("type");
        roomId = jsonObject.getString("roomId");
        userId = jsonObject.getString("userId");

        // Stats
        stats.fromJSON(jsonObject);
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = json().createObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("type", type);
        jsonObject.put("roomId", roomId);
        jsonObject.put("userId", userId);

        // Stats
        stats.toJSON(jsonObject);
        return jsonObject;
    }

    public void init() {
        // Load user id text
        userIdText = TextUtil.createMessageText(userId, 11, 0xFFFFFFFF);

        // Initialize health bar
        healthBar = PlayN.graphics().createImage(width - 16, 10);

        // Load sprite sheet images
        spriteSheet = AssetManager.getInstance().getImages().get("images/characters/" + type + "/" + type + ".png");
        animations = new HashMap<String, ArrayList<Image>>();
        loadAnimations();

        // Add to world if not the player
        if (!userId.equals(Player.getInstance().getUserId())) {
            if (!roomId.equals("")) {
                World.getInstance().getRooms().get(roomId).getRenderedObjects().add(this);
            }
        }
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

    public void setServerPosition(int serverX, int serverY) {
        if (x != serverX) x = serverX;
        if (y != serverY) y = serverY;
    }

    public void rest() {
        currentSpriteIndex = 0d;
    }

    @Override
    public Image getImage() {
        return getCurrentImage();
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
        return animations.get("walk_up").get((int) currentSpriteIndex / 10);
    }

    private void incrementSpriteIndex() {
        currentSpriteIndex += 2;
        if (currentSpriteIndex >= 90) {
            currentSpriteIndex = 0.0;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void setImage(Image image) {

    }

    @Override
    public void render(Surface surface) {
        super.render(surface);

        // Render health bar
        renderHealthBar(surface);

        // Render the userid text
        /*surface.drawImage(userIdText,
                getX() - userIdText.width() / 2,
                getY() - getImage().height() - userIdText.height() / 2);*/
    }

    private void renderHealthBar(Surface surface) {
        // Black background bar
        healthBar.canvas().setFillColor(0xFF000000);
        healthBar.canvas().fillRect(0, 0, healthBar.width(), healthBar.height());

        // Health bar
        healthBar.canvas().setFillColor(0xFF00AA00);
        healthBar.canvas().fillRect(0, 0, (float)(stats.getCurrentHP() / stats.getMaxHP()) * healthBar.width(), healthBar.height());

        // Health bar outline
        healthBar.canvas().setStrokeColor(0xFF000000);
        healthBar.canvas().setStrokeWidth(1f);
        healthBar.canvas().strokeRect(0, 0, healthBar.width(), healthBar.height() - 1);

        // Paint the health bar
        surface.drawImage(healthBar,
                getX() - healthBar.width() / 2,
                getY() - getImage().height());
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
