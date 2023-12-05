package project.model.authenticationModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import project.validation.confirmPassword.PasswordMatching;

@PasswordMatching(
        newPassword = "newPassword",
        confirmNewPassword = "confirmNewPassword",
        message = "Паролі мають бути однаковими"
)
public class ChangePasswordRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(min=8,  message = "Розмір паролю має бути не менше 8 символів")
    private String newPassword;
    @NotEmpty(message = "Поле не може бути порожнім")
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
