package project.model.userModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class UserResponse {
    @Schema(example = "1", required = true)
    private Long id;
    @Schema(example = "Софія", required = true)
    private String name;
    @Schema(example = "+380994526713", required = true)
    private String phoneNumber;
    @Schema(example = "2000-02-15", required = true)
    private LocalDate birthDate;
    @Schema(example = "Українська", required = true)
    private String language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
