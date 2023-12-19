package project.model.authenticationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class EmailRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    @Schema(example = "user1@gmail.com", required = true)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
