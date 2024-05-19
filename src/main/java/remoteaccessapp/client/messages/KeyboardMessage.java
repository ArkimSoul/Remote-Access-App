package remoteaccessapp.client.messages;

import java.io.Serializable;

public record KeyboardMessage(int keyCode, boolean isPressed) implements Serializable {
}
