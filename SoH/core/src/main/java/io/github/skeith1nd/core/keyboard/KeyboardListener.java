package io.github.skeith1nd.core.keyboard;

import io.github.skeith1nd.core.ui.UIManager;
import playn.core.Keyboard;

public class KeyboardListener implements Keyboard.Listener {
    private byte wasd = 0x00;

    @Override
    public void onKeyDown(Keyboard.Event event) {
        switch (event.key()) {
            case W:
                wasd |= 0x01;
                break;
            case A:
                wasd |= 0x02;
                break;
            case S:
                wasd |= 0x04;
                break;
            case D:
                wasd |= 0x08;
                break;
            case I:
                UIManager.getInstance().toggleInventory();
        }
    }

    @Override
    public void onKeyTyped(Keyboard.TypedEvent event) {

    }

    @Override
    public void onKeyUp(Keyboard.Event event) {
        switch (event.key()) {
            case W:
                wasd &= 0x0E;
                break;
            case A:
                wasd &= 0x0D;
                break;
            case S:
                wasd &= 0x0B;
                break;
            case D:
                wasd &= 0x07;
                break;
        }
    }

    public byte getWasd() {
        return wasd;
    }

    public void setWasd(byte wasd) {
        this.wasd = wasd;
    }
}
