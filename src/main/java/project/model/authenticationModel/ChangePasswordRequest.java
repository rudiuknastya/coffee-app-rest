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
    @NotEmpty
    @Size(min=8, max=10, message = "Розмір паролю має бути не менше 8 та не більше 10 символів")
    private String newPassword;
    @NotEmpty
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
