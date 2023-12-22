package project.validation.phoneNumberValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.entity.User;
import project.repository.UserRepository;

import java.util.Optional;

public class FieldPhoneUniqueValidator implements ConstraintValidator<FieldPhoneUnique, String> {
    private final UserRepository userRepository;

    public FieldPhoneUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userRepository.findByPhoneNumber(s);
        return user.isEmpty();
    }
}
