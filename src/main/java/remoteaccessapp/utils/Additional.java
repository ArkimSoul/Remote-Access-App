package remoteaccessapp.utils;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class Additional {

    public static int mouseKeyToInputKey(int mouseKey) {
        return switch (mouseKey) {
            case MouseEvent.BUTTON1 -> InputEvent.BUTTON1_DOWN_MASK;
            case MouseEvent.BUTTON2 -> InputEvent.BUTTON2_DOWN_MASK;
            case MouseEvent.BUTTON3 -> InputEvent.BUTTON3_DOWN_MASK;
            default -> 0;
        };
    }
}
