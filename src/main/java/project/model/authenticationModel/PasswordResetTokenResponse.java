package project.model.authenticationModel;

import io.swagger.v3.oas.annotations.media.Schema;

public class PasswordResetTokenResponse {
    @Schema(example = "66193a10-c1e9-4a1e-b938-9192a74926cb")
    private String passwordResetToken;

    public PasswordResetTokenResponse(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
}
