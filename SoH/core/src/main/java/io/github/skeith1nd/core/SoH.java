package io.github.skeith1nd.core;

import static playn.core.PlayN.*;

import io.github.skeith1nd.core.keyboard.KeyboardListener;
import io.github.skeith1nd.core.network.Client;
import io.github.skeith1nd.core.player.Player;
import io.github.skeith1nd.core.world.World;
import playn.core.*;

public class SoH extends Game.Default {
    private KeyboardListener keyboardListener;
    private ImmediateLayer gameLayer;

    public SoH() {
        super(33); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {
        // Init user
        Client.getInstance().connect();

        // Init input
        keyboardListener = new KeyboardListener();
        keyboard().setListener(keyboardListener);

        // create and add background image layer
        Image bgImage = assets().getImage("images/bg.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);

        gameLayer = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            public void render(Surface surface) {
                surface.clear();
                World.getInstance().paint(surface);
            }
        });
        graphics().rootLayer().add(gameLayer);
    }

    @Override
    public void update(int delta) {
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

    @Override
    public void paint(float alpha) {
        float sx = graphics().width() / (float)640;
        float sy = graphics().height() / (float)480;
        graphics().rootLayer().setScale(Math.min(sx, sy));
    }
}
