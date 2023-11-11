package project.model.authenticationModel;

import jakarta.validation.constraints.NotEmpty;
import project.validation.emailValidation.EmailExist;
import project.validation.emailValidation.EmailUnique;
import project.validation.emailValidation.FieldEmailUnique;

public class EmailRequest {
    @NotEmpty
    @EmailExist
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
