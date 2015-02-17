package io.github.skeith1nd.core.player;

import static playn.core.PlayN.*;

import io.github.skeith1nd.core.game.AssetManager;
import io.github.skeith1nd.core.network.Client;
import io.github.skeith1nd.core.util.TextUtil;
import io.github.skeith1nd.core.world.*;
import io.github.skeith1nd.network.core.commands.player.PlayerMoveCommand;
import playn.core.*;
import playn.core.Image;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends RenderedDynamic {
    private static Player instance;

    private Image spriteSheet, userIdText;
    private CanvasImage healthBar;
    private HashMap<String, ArrayList<Image>> animations;
    private double currentSpriteIndex;
    private byte control;
    private boolean resting;
    private String type, userId;
    private ClientRoom room;
    private int collisionWidth, collisionHeight;
    private PlayerStats stats;

    private Player() {
        x = y = 0;
        width = height = 64;
        collisionWidth = 20;
        collisionHeight = 10;
        type = userId = "";
        currentSpriteIndex = 0d;
        control = 0x08;
        resting = true;
        stats = new PlayerStats();
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

        // Stats
        stats.fromJSON(playerJSON);

        // Room
        room = new ClientRoom();
        room.fromJSON(roomJSON);
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = json().createObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("type", type);
        jsonObject.put("userId", userId);

        // Stats
        stats.toJSON(jsonObject);

        if (room != null) {
            jsonObject.put("roomId", room.getRoomId());
        }

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

        // Add to world
        if (room != null) {
            World.getInstance().getRooms().get(room.getRoomId()).getRenderedObjects().add(this);
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
        if (!checkForCollision(y - 3, PlayerMoveCommand.MOVE_UP)) {
            y -= 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_UP);
        }

        control = 0x01;
        resting = false;
        incrementSpriteIndex();
    }

    public void moveLeft(){
        if (!checkForCollision(x - 3, PlayerMoveCommand.MOVE_LEFT)) {
            x -= 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_LEFT);
        }

        control = 0x02;
        resting = false;
        incrementSpriteIndex();
    }

    public void moveDown(){
        if (!checkForCollision(y + 3, PlayerMoveCommand.MOVE_DOWN)) {
            y += 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_DOWN);
        }

        control = 0x04;
        resting = false;
        incrementSpriteIndex();
    }

    public void moveRight(){
        if (!checkForCollision(x + 3, PlayerMoveCommand.MOVE_RIGHT)) {
            x += 3;

            // Send move command to server
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.MOVE_RIGHT);
        }

        control = 0x08;
        resting = false;
        incrementSpriteIndex();
    }

    public void rest() {
        currentSpriteIndex = 0d;

        // Send move command to server if the last move wasn't resting
        if (!resting) {
            Client.getInstance().sendPlayerMoveCommand(PlayerMoveCommand.REST);
            resting = true;
        }
    }

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
        return animations.get("walk_up").get((int)currentSpriteIndex / 10);
    }

    private void incrementSpriteIndex() {
        currentSpriteIndex += 2;
        if (currentSpriteIndex >= 90) {
            currentSpriteIndex = 0.0;
        }
    }

    private boolean checkForCollision(int target, int direction) {
        int newX = x, newY = y;
        switch (direction) {
            case PlayerMoveCommand.MOVE_RIGHT:
                newX = target;
                break;
            case PlayerMoveCommand.MOVE_UP:
                newY = target;
                break;
            case PlayerMoveCommand.MOVE_DOWN:
                newY = target;
                break;
            case PlayerMoveCommand.MOVE_LEFT:
                newX = target;
                break;
        }

        // Loop through objects in room and check for collision
        for (CollisionObject object : World.getInstance().getRooms().get(room.getRoomId()).getCollisionObjects()) {
            CollisionRectangle playerRect = new CollisionRectangle(newX - collisionWidth / 2, newY - collisionHeight / 2, collisionWidth, collisionHeight);
            CollisionRectangle objectRect = new CollisionRectangle(object.getX() - object.getCollisionWidth() / 2, object.getY() - object.getCollisionHeight() / 2, object.getCollisionWidth(), object.getCollisionHeight());

            if (playerRect.intersects(objectRect)) {
                return true;
            }
        }
        return false;
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
    public void destroy() {

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

    public ClientRoom getRoom() {
        return room;
    }

    public void setRoom(ClientRoom room) {
        this.room = room;
    }
}
