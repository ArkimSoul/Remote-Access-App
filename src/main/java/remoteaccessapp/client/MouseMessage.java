package remoteaccessapp.client;

import java.io.Serializable;

public class MouseMessage implements Serializable {
    private int x;
    private int y;
    private int mouseButton;
    private boolean isPressed;

    public MouseMessage(int x, int y, int mouseButton, boolean isPressed) {
        this.x = x;
        this.y = y;
        this.mouseButton = mouseButton;
        this.isPressed = isPressed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMouseButton() {
        return mouseButton;
    }

    public boolean isPressed() {
        return isPressed;
    }
}
