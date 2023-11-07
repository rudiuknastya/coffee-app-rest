package project.validation.confirmPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import project.validation.confirmPassword.PasswordMatching;

import java.util.Objects;

public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, Object> {
    private String newPassword;
    private String confirmNewPassword;
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object passwordValue = new BeanWrapperImpl(s).getPropertyValue(newPassword);
        Object confirmPasswordValue = new BeanWrapperImpl(s).getPropertyValue(confirmNewPassword);
        if(passwordValue == null &&  confirmPasswordValue == null){
            return true;
        } else {
            return Objects.equals(passwordValue, confirmPasswordValue);
        }
    }

    @Override
    public void initialize(PasswordMatching matching) {
        this.newPassword = matching.newPassword();
        this.confirmNewPassword = matching.confirmNewPassword();
    }
}
