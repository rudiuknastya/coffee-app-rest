package project.model.authenticationModel;

import jakarta.validation.constraints.NotEmpty;

public class EmailRequest {
    @NotEmpty
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
