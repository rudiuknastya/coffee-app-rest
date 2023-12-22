package project.validation.notEmptyPasswordValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class NotEmptyOldPasswordValidator implements ConstraintValidator<NotEmptyOldPassword,Object> {
    private String oldPassword;
    private String newPassword;

    @Override
    public void initialize(NotEmptyOldPassword constraintAnnotation) {
        this.newPassword = constraintAnnotation.newPassword();
        this.oldPassword = constraintAnnotation.oldPassword();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object newPasswordValue = new BeanWrapperImpl(o).getPropertyValue(newPassword);
        Object oldPasswordValue = new BeanWrapperImpl(o).getPropertyValue(oldPassword);
        if(!newPasswordValue.equals("") && oldPasswordValue.equals("")){
            return false;
        }
        return true;
    }
}