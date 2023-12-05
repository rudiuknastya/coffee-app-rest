package project.model.authenticationModel;

import jakarta.validation.constraints.NotEmpty;

public class AuthenticationRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    private String email;
    @NotEmpty(message = "Поле не може бути порожнім")
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
