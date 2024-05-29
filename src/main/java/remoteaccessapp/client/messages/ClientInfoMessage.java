package remoteaccessapp.client.messages;

import java.io.Serializable;

public record ClientInfoMessage(byte[] deviceName) implements Serializable {
}
