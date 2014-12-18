package io.github.skeith1nd.core;

import static playn.core.PlayN.*;

import io.github.skeith1nd.core.keyboard.KeyboardListener;
import io.github.skeith1nd.core.world.World;
import playn.core.*;

public class SoH extends Game.Default {

    private KeyboardListener keyboardListener;


    private World world;
    private ImmediateLayer gameLayer;

    public SoH() {
        super(33); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {
        // Init input
        keyboardListener = new KeyboardListener();
        keyboard().setListener(keyboardListener);

        // create and add background image layer
        Image bgImage = assets().getImage("images/bg.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);

        world = new World();

        gameLayer = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
            public void render(Surface surface) {
                surface.clear();
                world.paint(surface);
            }
        });
        graphics().rootLayer().add(gameLayer);
    }

    @Override
    public void update(int delta) {
        byte control = keyboardListener.getWasd();
        if ((control & 0x01) == 0x01) {
            world.getPlayer().moveUp();
        }

        if ((control & 0x02) == 0x02) {
            world.getPlayer().moveLeft();
        }

        if ((control & 0x04) == 0x04) {
            world.getPlayer().moveDown();
        }

        if ((control & 0x08) == 0x08) {
            world.getPlayer().moveRight();
        }

        if ((control & 0x0F) == 0x00) {
            world.getPlayer().rest();
        }
    }

    @Override
    public void paint(float alpha) {
        // the background automatically paints itself, so no need to do anything here!
    }
}
