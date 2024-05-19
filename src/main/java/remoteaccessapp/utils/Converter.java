package remoteaccessapp.utils;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class Converter {
    public static int mouseKeyToInputKey(int mouseKey) {
        return switch (mouseKey) {
            case MouseEvent.BUTTON1 -> InputEvent.BUTTON1_DOWN_MASK;
            case MouseEvent.BUTTON2 -> InputEvent.BUTTON2_DOWN_MASK;
            case MouseEvent.BUTTON3 -> InputEvent.BUTTON3_DOWN_MASK;
            default -> 0;
        };
    }

    public static byte[] mergeBytes(byte[] bytes1, byte[] bytes2) {
        byte[] bytes = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, bytes, 0, bytes1.length);
        System.arraycopy(bytes2, 0, bytes, bytes1.length, bytes2.length);
        return bytes;
    }
}
