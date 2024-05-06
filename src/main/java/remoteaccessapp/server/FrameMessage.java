package remoteaccessapp.server;

import java.io.Serializable;

public class FrameMessage implements Serializable {
    private byte[] image;

    public FrameMessage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
