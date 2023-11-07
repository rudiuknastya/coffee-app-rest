package project.model.userModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import project.validation.confirmPassword.PasswordMatching;
import project.validation.emailValidation.EmailUnique;
import project.validation.phoneNumberValidation.PhoneNumberUnique;
import project.validation.validateOldPassword.OldPasswordMatching;

import java.time.LocalDate;
@PasswordMatching(
        newPassword = "newPassword",
        confirmNewPassword = "confirmNewPassword",
        message = "Паролі мають бути однаковими"
)
@OldPasswordMatching(
        id = "id",
        oldPassword = "oldPassword",
        message = "Невірний пароль"
)
@EmailUnique(
        id = "id",
        email = "email"
)
@PhoneNumberUnique(
        id = "id",
        phoneNumber = "phoneNumber"
)

public class UserProfileRequest {
    @Schema(example = "1", required = true)
    private Long id;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Schema(example = "Софія", required = true)
    private String name;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(min=4, max=15, message = "Розмір поля має бути не менше 4 та не більше 15 символів")
    @Pattern(regexp = "^\\+?[1-9][0-9]{4,15}$", message = "Невірний формат номеру")
    @Schema(example = "+380994526713", required = true)
    private String phoneNumber;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Email(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,3}", message = "Невірний формат email")
    private String email;
    @Schema(example = "2000-02-15")
    private LocalDate birthDate;
    @Schema(example = "Українська")
    private String language;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
