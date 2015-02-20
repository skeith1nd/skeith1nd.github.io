package io.github.skeith1nd.core.player;

import io.github.skeith1nd.core.game.AssetManager;
import io.github.skeith1nd.core.mouse.IMouseable;
import io.github.skeith1nd.core.mouse.MouseAdapter;
import io.github.skeith1nd.core.util.TextUtil;
import io.github.skeith1nd.core.world.RenderedDynamic;
import io.github.skeith1nd.core.world.World;
import playn.core.*;

import java.util.ArrayList;
import java.util.HashMap;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.json;

public abstract class PlayerEntity extends RenderedDynamic implements IMouseable {
    protected Image spriteSheet, userIdText;
    protected CanvasImage healthBar;
    protected ImageLayer healthBarLayer, playerLayer;
    protected HashMap<String, ArrayList<Image>> animations;
    protected double currentSpriteIndex;
    protected byte control;
    protected boolean resting;
    protected String type, userId;
    protected int collisionWidth, collisionHeight;
    protected PlayerStats stats;
    protected boolean initialized;

    protected PlayerEntity() {
        x = y = 0;
        width = height = 64;
        collisionWidth = 20;
        collisionHeight = 10;
        type = userId = "";
        currentSpriteIndex = 0d;
        control = 0x08;
        resting = true;
        stats = new PlayerStats();
        healthBarLayer = graphics().createImageLayer();
        playerLayer = graphics().createImageLayer();
        initialized = false;
    }

    public void fromJSON(Json.Object playerJSON) {
        x = playerJSON.getInt("x");
        y = playerJSON.getInt("y");
        type = playerJSON.getString("type");
        userId = playerJSON.getString("userId");

        // Stats
        stats.fromJSON(playerJSON);
    }

    public Json.Object toJSON() {
        Json.Object jsonObject = json().createObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("type", type);
        jsonObject.put("userId", userId);

        // Stats
        stats.toJSON(jsonObject);

        return jsonObject;
    }

    @Override
    public void init() {
        super.init();

        // Load user id text
        userIdText = TextUtil.createMessageText(userId, 11, 0xFFFFFFFF);

        // Initialize health bar
        healthBar = PlayN.graphics().createImage(width - 16, 8);
        healthBarLayer.setImage(healthBar);
        healthBarLayer.setTranslation(8, 0);

        // Load sprite sheet images
        spriteSheet = AssetManager.getInstance().getImages().get("images/characters/" + type + "/" + type + ".png");
        animations = new HashMap<String, ArrayList<playn.core.Image>>();
        loadAnimations();

        // Add image layers to group layer
        layer.add(healthBarLayer);
        layer.add(playerLayer);

        // Set up listeners
        playerLayer.addListener(new MouseAdapter(this));

        // Update layer
        initialized = true;
        updateLayer();
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

    public void moveUp() {
        control = 0x01;
        incrementSpriteIndex();
    }

    public void moveLeft() {
        control = 0x02;
        incrementSpriteIndex();
    }

    public void moveDown() {
        control = 0x04;
        incrementSpriteIndex();
    }

    public void moveRight() {
        control = 0x08;
        incrementSpriteIndex();
    }

    public void rest() {
        currentSpriteIndex = 0d;
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

    @Override
    public void updateLayer() {
        if (initialized) {
            super.updateLayer();

            // Update player
            playerLayer.setImage(getImage());

            // Update health bar
            renderHealthBar();
            healthBarLayer.setImage(healthBar);
        }
    }

    @Override
     public void mouseOver() {
        System.out.println("Mouse over " + userId);
    }

    @Override
    public void mouseMove() {
        System.out.println("Mouse move " + userId);
    }

    @Override
    public void mouseOut() {
        System.out.println("Mouse out " + userId);
    }

    @Override
    public void mouseLeftClick() {
        System.out.println("Mouse left click " + userId);
    }

    @Override
    public void mouseRightClick() {
        System.out.println("Mouse right click " + userId);
    }

    private void renderHealthBar() {
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
    }

    public Image getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(Image spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public Image getUserIdText() {
        return userIdText;
    }

    public void setUserIdText(Image userIdText) {
        this.userIdText = userIdText;
    }

    public CanvasImage getHealthBar() {
        return healthBar;
    }

    public void setHealthBar(CanvasImage healthBar) {
        this.healthBar = healthBar;
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

    public boolean isResting() {
        return resting;
    }

    public void setResting(boolean resting) {
        this.resting = resting;
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

    public int getCollisionWidth() {
        return collisionWidth;
    }

    public void setCollisionWidth(int collisionWidth) {
        this.collisionWidth = collisionWidth;
    }

    public int getCollisionHeight() {
        return collisionHeight;
    }

    public void setCollisionHeight(int collisionHeight) {
        this.collisionHeight = collisionHeight;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }
}
