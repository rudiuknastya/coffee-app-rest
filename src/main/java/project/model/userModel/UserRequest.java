package project.model.userModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import project.validation.emailValidation.FieldEmailUnique;
import project.validation.phoneNumberValidation.FieldPhoneUnique;

public class UserRequest {
    @NotEmpty(message = "Поле не може бути порожнім")
    @Size(max=25, message = "Розмір поля має бути не більше 25 символів")
    @Schema(example = "Вікторія", required = true)
    private String name;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=13, message = "Розмір номеру має бути не більше 13 символів")
    @Pattern(regexp = "\\+380(50|66|95|99|67|68|96|97|98|63|93|73)[0-9]{7}", message = "Невірний формат номеру")
    @FieldPhoneUnique
    @Schema(example = "+380665742314", required = true)
    private String phoneNumber;
    @NotEmpty(message = "Поле не може бути порожнім ")
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    @Email(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-z]{2,3}", message = "Невірний формат email")
    @FieldEmailUnique
    @Schema(example = "user1@gmail.com", required = true)
    private String email;
    @Size(max=100, message = "Розмір поля має бути не більше 100 символів")
    @Pattern.List({
            @Pattern(regexp = ".{8,}", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*\\d+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*[,./?]+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8"),
            @Pattern(regexp = ".*[A-Z]+.*", message = "Пароль має мати принаймні одну цифру, одну велику літеру, один спецсимвол ,./? та розмір більше 8")
    })
    @Schema(example = "Password/2", required = true)
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
