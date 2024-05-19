package remoteaccessapp.client.messages;

import java.io.Serializable;

public class KeyboardMessage implements Serializable {
    private int keyCode;
    private boolean isPressed;

    public KeyboardMessage(int keyCode, boolean isPressed) {
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
