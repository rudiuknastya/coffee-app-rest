package project.model.authenticationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import project.validation.confirmPassword.PasswordMatching;

@PasswordMatching(
        newPassword = "newPassword",
        confirmNewPassword = "confirmNewPassword",
        message = "Паролі мають бути однаковими"
)
public class ChangePasswordRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100,  message = "Розмір поля має бути не більше 100 символів")
    @Pattern.List({
            @Pattern(regexp = ".{8,}", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*\\d+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*[,./?]+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*[A-Z]+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8")
    })
    @Schema(example = "Password/2", required = true)
    private String newPassword;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=100,  message = "Розмір поля має бути не більше 100 символів")
    @Schema(example = "Password/2", required = true)
    private String confirmNewPassword;


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
}
