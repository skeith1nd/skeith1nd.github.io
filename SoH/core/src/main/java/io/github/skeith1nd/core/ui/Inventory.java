package io.github.skeith1nd.core.ui;

import io.github.skeith1nd.core.world.World;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.Path;

import static playn.core.PlayN.graphics;

public class Inventory {
    private static Inventory instance;

    // Inventory item size
    private int size;

    // Dimensions
    private int inventoryWidth = 6 * 30;
    private int inventoryHeight = 5 * 30;

    // Layer drawing
    private CanvasImage canvasImage;
    private ImageLayer imageLayer;
    private boolean showInventory;

    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    private Inventory() {
        size = 25;
    }

    public void init() {
        canvasImage = graphics().createImage(inventoryWidth, inventoryHeight);
        imageLayer = graphics().createImageLayer(canvasImage);
        World.getInstance().getUiLayer().add(imageLayer);
    }

    public void update() {
        if (showInventory) {
            renderInventory();
        }
    }

    public void renderInventory() {
        canvasImage.canvas().setFillColor(0x99000066);
        canvasImage.canvas().fillRect(0, 0, inventoryWidth, inventoryHeight);

        canvasImage.canvas().setStrokeColor(0x9900FFFF);
        canvasImage.canvas().setStrokeWidth(3);
        canvasImage.canvas().strokeRect(0, 0, inventoryWidth, inventoryHeight);

        Path line = canvasImage.canvas().createPath();
        canvasImage.canvas().setStrokeWidth(3);
        for (int i = 1; i < 6; i++) {
            line.moveTo(i * 30, 0);
            line.lineTo(i * 30, inventoryHeight);

            line.moveTo(0, 30 * i);
            line.lineTo(inventoryWidth, 30 * i);
        }

        line.moveTo(inventoryWidth, 0);
        line.lineTo(inventoryWidth, inventoryHeight);

        line.close();
        canvasImage.canvas().strokePath(line);
    }

    private void showInventory() {
        showInventory = true;
        World.getInstance().getUiLayer().add(imageLayer);
    }

    private void hideInventory() {
        showInventory = false;
        World.getInstance().getUiLayer().remove(imageLayer);
    }

    public void toggleInventory() {
        if (showInventory) {
            hideInventory();
        } else {
            showInventory();
        }
    }
}
