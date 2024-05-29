package remoteaccessapp.server.messages;

import java.io.Serializable;

public record ConfirmMessage(boolean confirm) implements Serializable {
}
