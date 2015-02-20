package io.github.skeith1nd.core.mouse;

import playn.core.Mouse;

public class MouseAdapter extends Mouse.LayerAdapter {
    private IMouseable object;

    public MouseAdapter(IMouseable object) {
        this.object = object;
    }

    @Override
    public void onMouseDown(Mouse.ButtonEvent event) {
        switch (event.button()) {
            case Mouse.BUTTON_LEFT:
                object.mouseLeftClick();
                break;
            case Mouse.BUTTON_RIGHT:
                object.mouseRightClick();
                break;
        }
    }

    @Override
    public void onMouseOver(Mouse.MotionEvent event) {
        object.mouseOver();
    }

    @Override
    public void onMouseMove(Mouse.MotionEvent event) {
        object.mouseMove();
    }

    @Override
    public void onMouseOut(Mouse.MotionEvent event) {
        object.mouseOut();
    }

    public IMouseable getObject() {
        return object;
    }

    public void setObject(IMouseable object) {
        this.object = object;
    }
}
