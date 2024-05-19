package remoteaccessapp.server.messages;

import java.io.Serializable;

public record RSAKeyMessage(String publicKey, String privateKey) implements Serializable {
}
