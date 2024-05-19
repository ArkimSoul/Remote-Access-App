package remoteaccessapp.server.messages;

import java.io.Serializable;

public record FrameMessage(byte[] image) implements Serializable {
}
