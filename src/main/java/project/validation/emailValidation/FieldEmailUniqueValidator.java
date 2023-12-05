package project.validation.emailValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.entity.User;
import project.repository.UserRepository;

public class FieldEmailUniqueValidator implements ConstraintValidator<FieldEmailUnique,String> {
    private final UserRepository userRepository;

    public FieldEmailUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(!userRepository.findByEmail(s).isPresent()){
            return true;
        }
        return false;
    }
}
