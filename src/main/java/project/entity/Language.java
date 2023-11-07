package project.entity;

public enum Language {
    ENG("Англійська"), UKR("Українська"), SPA("Іспанська");

    Language(String languageName) {
        this.languageName = languageName;
    }

    private String languageName;

    public String getLanguageName() {
        return languageName;
    }
    public static Language fromString(String text) {
        for (Language l : Language.values()) {
            if (l.languageName.equalsIgnoreCase(text)) {
                return l;
            }
        }
        return null;
    }
}
