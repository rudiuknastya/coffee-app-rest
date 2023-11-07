package project.validation.phoneNumberValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.entity.User;
import project.repository.UserRepository;

public class FieldPhoneUniqueValidator implements ConstraintValidator<FieldPhoneUnique, String> {
    private final UserRepository userRepository;

    public FieldPhoneUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        User user = userRepository.findByPhoneNumber(s);
        if(user == null){
            return true;
        }
        return false;
    }
}
