package remoteaccessapp.client;

import java.io.Serializable;

public class KeyMessage implements Serializable {
    private int keyCode;
    private boolean isPressed;

    public KeyMessage(int keyCode, boolean isPressed) {
        this.keyCode = keyCode;
        this.isPressed = isPressed;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isPressed() {
        return isPressed;
    }
}
