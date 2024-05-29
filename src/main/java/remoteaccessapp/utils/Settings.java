package remoteaccessapp.utils;

import remoteaccessapp.Instance;
import remoteaccessapp.enums.Language;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import java.util.prefs.Preferences;

public class Settings {
    private final Instance instance;

    private final Preferences preferences = Preferences.userRoot().node("remoteaccessapp");

    public Settings(Instance inst) {
        instance = inst;
    }

    public void restoreDefaults() {
        try {
            preferences.clear();
            getDeviceName();
            getLanguage();
            getServerPort();
            isAESEnabled();
            isRSAEnabled();
            getAESKeyRenewalPeriod();
        }
        catch (Exception _) {

        }
    }

    public String getDeviceName() {
        if (preferences.get("deviceName", null) == null) {
            try {
                return InetAddress.getLocalHost().getHostName();
            }
            catch (Exception _) {
                return "Unknown Device";
            }
        }
        else {
            return preferences.get("deviceName", null);
        }
    }

    public void setDeviceName(String deviceName) {
        preferences.put("deviceName", deviceName);
    }

    public Language getLanguage() {
        String language = preferences.get("language", Language.ENGLISH.getCode());
        for (Language lang : Language.values()) {
            if (lang.getCode().equals(language)) {
                return lang;
            }
        }
        return Language.ENGLISH;
    }

    public void setLanguage(Language language) {
        preferences.put("language", language.getCode());
        instance.updateLanguage();
    }

    public int getServerPort() {
        return Integer.parseInt(preferences.get("port", "4389"));
    }

    public void setServerPort(int port) {
        preferences.put("port", String.valueOf(port));
    }

    public boolean isAESEnabled() {
        return Boolean.parseBoolean(preferences.get("aesEnabled", "false"));
    }

    public void setAESEnabled(boolean aesEnabled) {
        preferences.put("aesEnabled", String.valueOf(aesEnabled));
    }

    public boolean isRSAEnabled() {
        return Boolean.parseBoolean(preferences.get("rsaEnabled", "false"));
    }

    public void setRSAEnabled(boolean rsaEnabled) {
        preferences.put("rsaEnabled", String.valueOf(rsaEnabled));
    }

    public int getAESKeyRenewalPeriod() {
        return Integer.parseInt(preferences.get("aesKeyRenewalPeriod", "180"));
    }

    public void setAESKeyRenewalPeriod(int i) {
        preferences.put("aesKeyRenewalPeriod", String.valueOf(i));
    }
}
