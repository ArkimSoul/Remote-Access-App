package remoteaccessapp.server.messages;

import java.io.Serializable;

public class AESKeyMessage implements Serializable {
    private String key;

    public AESKeyMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
