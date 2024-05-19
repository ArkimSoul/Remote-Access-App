package remoteaccessapp.utils.enums;

public enum Language {
    ENGLISH ("English", "en_US"),
    RUSSIAN ("Russian", "ru_RU"),
    KAZAKH ("Kazakh", "kk_KZ");

    private String title;
    private String code;

    Language(String title, String code) {
        this.title = title;
        this.code = code;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getCode() {
        return code;
    }
}
