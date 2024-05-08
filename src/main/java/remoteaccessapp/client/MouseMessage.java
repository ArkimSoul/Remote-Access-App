package remoteaccessapp.client;

import java.io.Serializable;

public class MouseMessage implements Serializable {
    private int x;
    private int y;
    private int wheelRotation;
    private int mouseButton;
    private boolean isPressed;
    private boolean isWheel = false;

    public MouseMessage(int x, int y, int mouseButton, boolean isPressed) {
        this.x = x;
        this.y = y;
        this.mouseButton = mouseButton;
        this.isPressed = isPressed;
    }

    public MouseMessage(int wheelRotation, boolean isWheel) {
        this.wheelRotation = wheelRotation;
        this.isWheel = isWheel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWheelRotation() {
        return wheelRotation;
    }

    public int getMouseButton() {
        return mouseButton;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public boolean isWheel() {
        return isWheel;
    }
}
