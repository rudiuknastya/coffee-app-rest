package project.model.userModel;

import io.swagger.v3.oas.annotations.media.Schema;
import project.entity.Language;

public class LanguageResponse {

    private Language language;
    @Schema(example = "Українська", required = true)
    private String languageName;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}
