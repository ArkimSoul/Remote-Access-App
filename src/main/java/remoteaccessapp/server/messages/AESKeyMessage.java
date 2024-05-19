package remoteaccessapp.server.messages;

import java.io.Serializable;

public record AESKeyMessage(byte[] key) implements Serializable {
}
