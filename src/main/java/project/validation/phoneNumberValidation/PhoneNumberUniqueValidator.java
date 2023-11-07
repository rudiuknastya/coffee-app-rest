package project.validation.phoneNumberValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import project.entity.User;
import project.model.userModel.UserProfileRequest;
import project.repository.UserRepository;

public class PhoneNumberUniqueValidator implements ConstraintValidator<PhoneNumberUnique, Object> {
    private final UserRepository userRepository;

    public PhoneNumberUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private String id;
    private String phoneNumber;

    @Override
    public void initialize(PhoneNumberUnique constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.phoneNumber = constraintAnnotation.phoneNumber();
    }

    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);
        Object phoneValue = new BeanWrapperImpl(s).getPropertyValue(phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneValue.toString());
        if(user != null && !user.getId().equals((Long) idValue)){
            return false;
        }
        return true;
    }
}
