package project.model.authenticationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class AuthenticationRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    @Schema(example = "user1@gmail.com", required = true)
    private String email;
    @NotEmpty(message = "Поле не може бути порожнім")
    @Schema(example = "Password/2", required = true)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
