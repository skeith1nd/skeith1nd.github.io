package io.github.skeith1nd.core.ui;

public class UIManager {
    private static UIManager instance;
    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    // State
    private boolean showButtons;

    private UIManager(){
        showButtons = true;

    }

    public void init() {
        Inventory.getInstance().init();
        update();
    }

    public void update() {
        Inventory.getInstance().update();
    }
}
