package project.model.userModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import project.validation.emailValidation.FieldEmailUnique;
import project.validation.phoneNumberValidation.FieldPhoneUnique;

public class UserRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    private String name;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(min=4, max=15, message = "Розмір поля має бути не менше 4 та не більше 15 символів")
    @Pattern(regexp = "^\\+?[1-9][0-9]{4,15}$", message = "Невірний формат номеру")
    @FieldPhoneUnique
    private String phoneNumber;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Email(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,3}", message = "Невірний формат email")
    @FieldEmailUnique
    private String email;
    @Size(min=8, max=10, message = "Розмір паролю має бути не менше 8 та не більше 10 символів")
    private String password;

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
