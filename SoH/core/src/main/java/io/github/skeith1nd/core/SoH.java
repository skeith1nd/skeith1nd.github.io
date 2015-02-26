package io.github.skeith1nd.core;

import static playn.core.PlayN.*;

import io.github.skeith1nd.core.game.AssetManager;
import io.github.skeith1nd.core.keyboard.KeyboardListener;
import io.github.skeith1nd.core.mouse.MouseListener;
import io.github.skeith1nd.core.network.Client;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.core.ui.UIManager;
import io.github.skeith1nd.core.world.World;
import playn.core.*;
import playn.core.util.Clock;

public class SoH extends Game.Default {
    private KeyboardListener keyboardListener;
    private boolean running = false;
    private final Clock.Source clock = new Clock.Source(25);

    public SoH() {
        super(25); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {
        // Load all assets
        AssetManager.getInstance().init(this);
    }

    @Override
    public void update(int delta) {
        clock.update(delta);

        if (running) {
            byte control = keyboardListener.getWasd();
            if ((control & 0x01) == 0x01) {
                Player.getInstance().moveUp();
            }

            if ((control & 0x02) == 0x02) {
                Player.getInstance().moveLeft();
            }

            if ((control & 0x04) == 0x04) {
                Player.getInstance().moveDown();
            }

            if ((control & 0x08) == 0x08) {
                Player.getInstance().moveRight();
            }

            if ((control & 0x0F) == 0x00) {
                Player.getInstance().rest();
            }
        }
    }

    @Override
    public void paint(float alpha) {
        float sx = graphics().width() / (float)640;
        float sy = graphics().height() / (float)480;
        graphics().rootLayer().setScale(Math.min(sx, sy));
    }

    public void start() {
        // Init world
        World.getInstance().init();

        // Init user
        Client.getInstance().connect();

        // Init UI
        UIManager.getInstance().init();

        // Init input
        keyboardListener = new KeyboardListener();
        keyboard().setListener(keyboardListener);

        running = true;
    }
}
