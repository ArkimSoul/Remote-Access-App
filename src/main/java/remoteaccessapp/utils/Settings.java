package remoteaccessapp.utils;

import remoteaccessapp.Instance;
import remoteaccessapp.utils.enums.Language;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private Instance instance;

    private static final String PROPERTIES_FILE_URL = "settings.properties";

    private Properties properties = new Properties();

    public Settings(Instance inst) {
        instance = inst;
        loadSettings();
    }

    private void loadSettings() {
        try {
            FileInputStream fis = new FileInputStream(PROPERTIES_FILE_URL);
            properties.load(fis);
        } catch (IOException e) {
            saveSettings();
        }
    }

    private void saveSettings() {
        try {
            FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE_URL);
            properties.store(fos, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDeviceName() {
        return properties.getProperty("deviceName", "Remote Access App");
    }

    public void setDeviceName(String deviceName) {
        properties.setProperty("deviceName", deviceName);
        saveSettings();
    }

    public Language getLanguage() {
        String language = properties.getProperty("language", Language.ENGLISH.getCode());
        for (Language lang : Language.values()) {
            if (lang.getCode().equals(language)) {
                return lang;
            }
        }
        return Language.ENGLISH;
    }

    public void setLanguage(Language language) {
        properties.setProperty("language", language.getCode());
        saveSettings();
        instance.updateLanguage();
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("port", "4389"));
    }

    public void setPort(int port) {
        properties.setProperty("port", String.valueOf(port));
        saveSettings();
    }

    public boolean getAESEnabled() {
        return Boolean.parseBoolean(properties.getProperty("aesEnabled", "false"));
    }

    public void setAESEnabled(boolean aesEnabled) {
        properties.setProperty("aesEnabled", String.valueOf(aesEnabled));
        saveSettings();
    }

    public boolean getRSAEnabled() {
        return Boolean.parseBoolean(properties.getProperty("rsaEnabled", "false"));
    }

    public void setRSAEnabled(boolean rsaEnabled) {
        properties.setProperty("rsaEnabled", String.valueOf(rsaEnabled));
        saveSettings();
    }
}
