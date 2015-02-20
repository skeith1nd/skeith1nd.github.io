package io.github.skeith1nd.core.ui;

import io.github.skeith1nd.core.world.World;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.Path;

import static playn.core.PlayN.*;

public class UIManager {
    private static UIManager instance;
    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    private CanvasImage inventoryCanvasImage;
    private ImageLayer inventoryImageLayer;

    // State
    private boolean showButtons;
    private boolean showInventory;

    // Dimensions
    private int inventoryWidth = 300;
    private int inventoryHeight = 350;

    private UIManager(){
        showButtons = true;
        showInventory = true;
    }

    public void init() {
        // Inventory
        inventoryCanvasImage = graphics().createImage(inventoryWidth, inventoryHeight);
        inventoryImageLayer = graphics().createImageLayer(inventoryCanvasImage);
        World.getInstance().getUiLayer().add(inventoryImageLayer);

        update();
    }

    public void update() {
        if (showInventory)
            renderInventory();
    }

    public void renderInventory() {
        inventoryCanvasImage.canvas().setFillColor(0x99000066);
        inventoryCanvasImage.canvas().fillRect(0, 0, inventoryWidth, inventoryHeight);

        inventoryCanvasImage.canvas().setStrokeColor(0x9900FFFF);
        inventoryCanvasImage.canvas().setStrokeWidth(1);
        inventoryCanvasImage.canvas().strokeRect(0, 0, inventoryWidth, inventoryHeight);

        Path line = inventoryCanvasImage.canvas().createPath();
        line.moveTo(50, 0);
        line.lineTo(50, inventoryHeight);
        line.moveTo(100, 0);
        line.lineTo(100, inventoryHeight);
        line.moveTo(150, 0);
        line.lineTo(150, inventoryHeight);
        line.close();
        inventoryCanvasImage.canvas().strokePath(line);
    }

    private void showInventory() {
        showInventory = true;
        World.getInstance().getUiLayer().add(inventoryImageLayer);
    }

    private void hideInventory() {
        showInventory = false;
        World.getInstance().getUiLayer().remove(inventoryImageLayer);
    }

    public void toggleInventory() {
        if (showInventory) {
            hideInventory();
        } else {
            showInventory();
        }
    }
}
