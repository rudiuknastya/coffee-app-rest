package project.validation.emailValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.entity.User;
import project.repository.UserRepository;

import java.util.Optional;

public class EmailExistValidator implements ConstraintValidator<EmailExist,String> {
    private final UserRepository userRepository;

    public EmailExistValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userRepository.findByEmail(s);
        System.out.println(user);
        if(user.isPresent()){
            return false;
        }
        return true;

    }
}
