package remoteaccessapp.utils;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Base64;

public class Converter {
    public static int mouseKeyToInputKey(int mouseKey) {
        return switch (mouseKey) {
            case MouseEvent.BUTTON1 -> InputEvent.BUTTON1_DOWN_MASK;
            case MouseEvent.BUTTON2 -> InputEvent.BUTTON2_DOWN_MASK;
            case MouseEvent.BUTTON3 -> InputEvent.BUTTON3_DOWN_MASK;
            default -> 0;
        };
    }

    public static String bytesToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] stringToBytes(String string) {
        return string.getBytes();
    }
}
