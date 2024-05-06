package remoteaccessapp.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ScreenRecorder {
    private static Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    private static BufferedImage screenImage;

    public static byte[] getByteFrame() {
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(makeScreenshot(), "png", byteArrayInputStream);
            return byteArrayInputStream.toByteArray();
        }
        catch (Exception e) {
        }
        return null;
    }

    private static BufferedImage makeScreenshot() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
        screenImage = robot.createScreenCapture(screenRect);
        return screenImage;
    }
}
