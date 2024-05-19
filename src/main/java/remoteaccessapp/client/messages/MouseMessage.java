package remoteaccessapp.client.messages;

import remoteaccessapp.enums.MouseAction;

import java.io.Serializable;

public record MouseMessage(int x, int y, int mouseButton, int wheelRotation, MouseAction mouseAction) implements Serializable {
}
